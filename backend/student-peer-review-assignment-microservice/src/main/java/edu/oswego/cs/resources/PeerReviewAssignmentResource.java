package edu.oswego.cs.resources;

import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import edu.oswego.cs.daos.FileDAO;
import edu.oswego.cs.database.PeerReviewAssignmentInterface;
import edu.oswego.cs.distribution.AssignmentDistribution;
import org.bson.Document;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("assignments")
public class PeerReviewAssignmentResource {

    @GET
    @RolesAllowed("professor")
    @Path("{courseID}/{assignmentID}/assign/{count_to_review}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignTeams(@PathParam("courseID") String courseID, @PathParam("assignmentID") int assignmentID, @PathParam("count_to_review") int count) throws Exception {
        PeerReviewAssignmentInterface peerReviewAssignmentInterface = new PeerReviewAssignmentInterface();

        List<String> teamNames = peerReviewAssignmentInterface.getCourseStudentIDs(courseID);
        Map<String, List<String>> assignedTeams;
        try {
            assignedTeams = AssignmentDistribution.distribute(teamNames, count);
        } catch (IndexOutOfBoundsException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Number of reviews peer team is greater than the number of teams in the course.").build();
        }

//        FileDAO.zipPeerReview(assignedTeams, courseID, assignmentID);

        return Response.status(Response.Status.OK).build();
    }

    /**
     *  An endpoint for a team to download another team's assignment submission to be peer reviewed.
     * @param courseID The course id that assigned the peer review.
     * @param assignmentID The assignment that the peer review is for
     * @param teamName The team that submitted an assignment that is need of a peer review
     * @return A file that contains the team's submitted assignment
     * @throws WebApplicationException A endpoint parameter error
     */
    @GET
    @RolesAllowed("student")
    @Path("{courseID}/{assignmentID}/{teamName}/download")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response downloadOtherTeamsAssignment(
            @PathParam("courseID") String courseID,
            @PathParam("assignmentID") int assignmentID,
            @PathParam("teamName") String teamName
    ) {

        File file = new File(FileDAO.peer_review_path + courseID + "/"+assignmentID+"/for-"+teamName.concat(".pdf"));

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition", "attachment; filename=" + file.getName());
        return response.build();
    }

    /**
     * An endpoint for uploading a peer review for another team's uploaded assignment.
     * @param attachments Multipart-Form data that contains the uploaded file content.
     * @param courseID The course id that assigned the peer review.
     * @param assignmentID The assignment that the peer review is for
     * @param srcTeamName The team that is reviewing the assignment
     * @param destTeamName The team that is receiving the peer review
     * @return OK response if the file was successfully added
     * @throws WebApplicationException A endpoint parameter error
     */
    @POST
    @RolesAllowed("student")
    @Path("{courseID}/{assignmentID}/{srcTeamName}/{destTeamName}/upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_OCTET_STREAM})
    @Produces({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_OCTET_STREAM})
    public Response uploadPeerReview(
            List<IAttachment> attachments,
            @PathParam("courseID") String courseID,
            @PathParam("assignmentID") int assignmentID,
            @PathParam("srcTeamName") String srcTeamName,
            @PathParam("destTeamName") String destTeamName
    ) throws IOException {
        PeerReviewAssignmentInterface peerReviewAssignmentInterface = new PeerReviewAssignmentInterface();
        for (IAttachment attachment : attachments) {
            if (attachment == null) continue;
            String fileName = attachment.getDataHandler().getName();
            if (!fileName.endsWith("pdf")) return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).build();
            peerReviewAssignmentInterface.uploadPeerReview(courseID, assignmentID, srcTeamName, destTeamName, attachment);
        }
        return Response.status(Response.Status.OK).entity("Successfully uploaded peer review.").build();
    }

    /**
     * Endpoint for a team to download the peer reviews that were made for the assignment that the team submitted.
     * The downloaded file from this endpoint is from another teams submitted peer review
     * @param courseID The course that assigned the peer review
     * @param assignmentID The assignment that the peer review is for
     * @param srcTeamName The team that reviewed the assignment
     * @param destTeamName The team that is receiving the peer review
     * @return A file that contains the peer review for the team's submitted assignment
     * @throws WebApplicationException A endpoint parameter error
     */
    @GET
    @RolesAllowed("student")
    @Path("{courseID}/{assignmentID}/{srcTeamName}/{destTeamName}/download")
    @Produces({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_OCTET_STREAM})
    public Response downloadFinishedTeamPeerReview(
        @PathParam("courseID") String courseID,
        @PathParam("assignmentID") int assignmentID,
        @PathParam("srcTeamName") String srcTeamName,
        @PathParam("destTeamName") String destTeamName
        ) {
        PeerReviewAssignmentInterface peerReviewAssignmentInterface = new PeerReviewAssignmentInterface();


        // check if the peer review due date is past
        // if not then return a response saying peer review is not ready

        File file = peerReviewAssignmentInterface.downloadFinishedPeerReview(courseID, assignmentID, srcTeamName, destTeamName);

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition", "attachment; filename=" + "peer-review-" + file.getName());
        return response.build();
    }
    @GET
    @RolesAllowed("student")
    @Path("{course-id}/{assignment-id}/{student-id}/reviewed-by-me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewUserReviewedAssignments(@PathParam("course-id") String courseID,
                                                @PathParam("assignment-id") int assignmentID,
                                                @PathParam("student-id") String teamName)
    {
        List<Document> documents = new PeerReviewAssignmentInterface().getAssignmentsReviewedByUser(courseID, assignmentID, teamName);
        return Response.status(Response.Status.OK).entity(documents).build();
    }

    @GET
    @RolesAllowed("student")
    @Path("{course-id}/{assignment-id}/{student-id}/my-graded-assignments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAssignmentsReviewedOfUser(@PathParam("course-id") String courseID,
                                                  @PathParam("assignment-id") int assignmentID,
                                                  @PathParam("student-id") String teamName)
    {
        List<Document> documents = new PeerReviewAssignmentInterface().getUsersGradedAssignments(courseID, assignmentID, teamName);
        return Response.status(Response.Status.OK).entity(documents).build();
    }

}