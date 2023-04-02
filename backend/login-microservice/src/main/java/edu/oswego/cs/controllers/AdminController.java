package edu.oswego.cs.controllers;

import edu.oswego.cs.database.AdminInterface;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

@Path("/admin")
// @RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminController {

    // Delete Admin User by User id and all associated data
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/admin/{user_id}")
    public Response deleteAdminUser(@Context SecurityContext securityContext, @PathParam("user_id") String userId)
            throws IOException {
        new AdminInterface(userId).deleteAdminUser(userId);
        return Response.status(Response.Status.OK).entity("Admin user deleted.").build();
    }

    // Delete Student User by User id and all associated data.
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/student/{user_id}")
    public Response deleteStudentUser(@Context SecurityContext securityContext, @PathParam("user_id") String userId) {
        new AdminInterface(userId).deleteStudentUser(userId);
        return Response.status(Response.Status.OK).entity("Student user deleted.").build();
    }

    // Delete Professor User by User id and all associated data.
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/professor/{user_id}")
    public Response deleteProfessorUser(@Context SecurityContext securityContext, @PathParam("user_id") String userId) {
        new AdminInterface(userId).deleteProfessorUser(userId);
        return Response.status(Response.Status.OK).entity("Professor user deleted.").build();
    }

    // Add Admin User by User Id, First and Last Name
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add/admin/{user_id}/{first_name}/{last_name}")
    public Response addAdminUser(@Context SecurityContext securityContext, @PathParam("user_id") String userId,
            @PathParam("first_name") String firstName, @PathParam("last_name") String lastName) {
        new AdminInterface(userId).addAdminUser(firstName, lastName, userId);
        return Response.status(Response.Status.OK).entity("Admin user added.").build();
    }

    // Add Student User by User Id, First and Last Name
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add/student/{user_id}/{first_name}/{last_name}")
    public Response addStudentUser(@Context SecurityContext securityContext, @PathParam("user_id") String userId,
            @PathParam("first_name") String firstName, @PathParam("last_name") String lastName) {
        System.out.println("Adding student user");
        new AdminInterface(userId).addStudentUser(firstName, lastName, userId);
        return Response.status(Response.Status.OK).entity("Student user added.").build();
    }

    // Add Admin User by User Id, First and Last Name
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add/professor/{user_id}/{first_name}/{last_name}")
    public Response addProfessorUser(@Context SecurityContext securityContext, @PathParam("user_id") String userId,
            @PathParam("first_name") String firstName, @PathParam("last_name") String lastName) {
        new AdminInterface(userId).addProfessorUser(firstName, lastName, userId);
        return Response.status(Response.Status.OK).entity("Professor user added.").build();
    }

    // Promote Professor User to Admin User by User Id
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/promote/professorToAdmin/{user_id}")
    public Response promoteProfessorToAdmin(@Context SecurityContext securityContext,
            @PathParam("user_id") String userId) {
        new AdminInterface(userId).promoteProfessorToAdmin(userId);
        return Response.status(Response.Status.OK).entity("Admin role added to professor user.").build();
    }

    // Promote Student User to Professor User by User Id
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/promote/studentToProfessor/{user_id}")
    public Response promoteStudentToProfessor(@Context SecurityContext securityContext,
            @PathParam("user_id") String userId) {
        new AdminInterface(userId).promoteStudentToProfessor(userId);
        return Response.status(Response.Status.OK).entity("Student promoted to professor role.").build();
    }

    // Promote Student User to Admin User by User Id
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/promote/studentToAdmin/{user_id}")
    public Response promoteStudentToAdmin(@Context SecurityContext securityContext,
            @PathParam("user_id") String userId) {
        new AdminInterface(userId).promoteStudentToAdmin(userId);
        return Response.status(Response.Status.OK).entity("Student promoted to admin role.").build();
    }

    // Demote Professor User to Student User by User Id
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/demote/professorToStudent/{user_id}")
    public Response demoteProfessorToStudent(@Context SecurityContext securityContext,
            @PathParam("user_id") String userId) {
        new AdminInterface(userId).demoteProfessorToStudent(userId);
        return Response.status(Response.Status.OK).entity("Admin role removed from professor user.").build();
    }

    // Demote Admin User to Professor User by User Id
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/demote/adminToProfessor/{user_id}")
    public Response demoteAdminToProfessor(@Context SecurityContext securityContext,
            @PathParam("user_id") String userId) {
        new AdminInterface(userId).demoteAdminToProfessor(userId);
        return Response.status(Response.Status.OK).entity("Admin role removed from professor user.").build();
    }

    // Demote Admin User to Student User by User Id
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/demote/adminToStudent/{user_id}")
    public Response demoteAdminToStudent(@Context SecurityContext securityContext,
            @PathParam("user_id") String userId) {
        new AdminInterface(userId).demoteAdminToStudent(userId);
        return Response.status(Response.Status.OK).entity("Admin role removed from student user.").build();
    }

    // Add Blocked Word
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/profanity/add/blocked/{word}")
    public Response addBlockedWord(@Context SecurityContext securityContext, @PathParam("word") String word) throws Exception {
        new AdminInterface().addBlockedWord(word);
        return Response.status(Response.Status.OK).entity("Blocked word added.").build();
    }

    // Delete Blocked Word
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/profanity/delete/blocked/{word}")
    public Response deleteBlockedWord(@Context SecurityContext securityContext, @PathParam("word") String word) throws Exception {
        new AdminInterface().deleteBlockedWord(word);
        return Response.status(Response.Status.OK).entity("Blocked word deleted.").build();
    }

    // Add Allowed Word
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/profanity/add/allowed/{word}")
    public Response addAllowedWord(@Context SecurityContext securityContext, @PathParam("word") String word) throws Exception {
        new AdminInterface().addAllowedWord(word);
        return Response.status(Response.Status.OK).entity("Allowed word added.").build();
    }

    // Remove Allowed Word
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/profanity/delete/allowed/{word}")
    public Response deleteAllowedWord(@Context SecurityContext securityContext, @PathParam("word") String word) throws Exception {
        new AdminInterface().deleteAllowedWord(word);
        return Response.status(Response.Status.OK).entity("Allowed word deleted.").build();
    }

    // Get Profanity Settings View
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/views/profanity")
    public Response getProfanitySettingsView(@Context SecurityContext securityContext) {
        return Response.status(Response.Status.OK).entity(new AdminInterface().getProfanitySettings()).build();
    }

    // // Get Users View
    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // @Path("/views/users")
    // public Response getUsersView(@Context SecurityContext securityContext) {
    //     return Response.status(Response.Status.OK).entity(new AdminInterface().getUsersView()).build();
    // }

    // Get Courses view
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/views/courses")
    public Response getCoursesView(@Context SecurityContext securityContext) {
        return Response.status(Response.Status.OK).entity(new AdminInterface().getCoursesView()).build();
    }
}
