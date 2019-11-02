package fr.pantheonsorbonne.ufr27.miage.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.google.common.io.ByteStreams;

import fr.pantheonsorbonne.ufr27.miage.DiplomaInfo;
import fr.pantheonsorbonne.ufr27.miage.persistence.DiplomaRepository;

@Path("diploma")
public class DiplomaResource {

	@Inject
	DiplomaRepository repository;


	@Path("{diplomaid}")
	@GET
	@Produces(value = { MediaType.APPLICATION_OCTET_STREAM })
	public Response getDiploma(@PathParam("diplomaid") int id) throws IOException {

		byte[] data = repository.getDiploma(id);
		if (data == null) {
			return Response.status(404).build();
		}
		if (data.length > 0) {

			return Response.accepted()//
					.header("Content-disposition", "attachment; filename=\"diploma.pdf\"")//
					.header("Content-Length", data.length)//
					.entity(data).build();

		} else {
			return Response.status(425).build();
		}
	}

	@POST
	@Consumes(value = { MediaType.APPLICATION_XML })
	public Response createDiploma(DiplomaInfo info, @Context UriInfo uri) {

		byte[] data = repository.getDiploma(info);
		URI location = UriBuilder.fromUri(uri.getRequestUri()).path("" + info.getId()).build();
		if (data == null || data.length == 0) {

			return Response.accepted().location(location).build();
		} else {
			return Response.status(303).location(location).build();
		}
	}
}
