package com.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.Remote.UsuarioBeanRemote;
import com.entidades.Observacion;
import com.entidades.TipoUsuario;
import com.entidades.Usuario;
import com.dao.*;

@Path("usuarios")
public class UsuariosRest {
	
	@EJB
	private UsuarioBeanRemote usuarioBeanRemote;
	
	@EJB TipoUsuariodao tipousuariodao;
	
	//Listado de usuarios
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> getUsuarios() {
		return usuarioBeanRemote.obtenerUsuarios();
	}
	
	//Usuarios por Id
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario getUsuarioById(@PathParam("id") Long id) {
		return usuarioBeanRemote.obtenerUsuarioPorId(id);
	}

	//Login de usuario 	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response loginUsuario(Usuario usuario){
		if (usuarioBeanRemote.Login(usuario.getUsuario(), usuario.getPass()).size() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Datos invalidos\"}").build();
		} else {
			return Response.ok().entity("{\"message\":\"Login exitoso\"}").build();
		}
	}
	
	//Agregar un usuario
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response addUsuario(Usuario usuario) {
	try {
		//TipoUsuario tipousu=tipousuariodao.obtenertipousuario(tipousuario);
	usuarioBeanRemote.CrearUsuario(usuario.getPass(),usuario.getUsuario(),usuario.getNombre(),usuario.getApellido(),usuario.getEstado(),usuario.getTipodoc(),usuario.getNumerodoc(),usuario.getDireccion(),usuario.getMail(),usuario.getTipousuario().getNombre());
	return Response.ok().entity("{\"message\":\"Alta Usuario exitosa\"}").build();
	}catch(Exception e)
		{
		e.printStackTrace();
		return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\": \"No se puede realizar el alta datos invalidos.\"}").build();
		}
	}
	
	//Modificar Usuario
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response updateUsuario(@PathParam("id") Long id, Usuario usuario) {
	try {

		usuarioBeanRemote.ModificarUsuario(usuario.getId(), usuario.getPass(), usuario.getUsuario(), usuario.getNombre(), 
				usuario.getApellido(), usuario.getEstado(), usuario.getTipodoc(), usuario.getNumerodoc(), 
				usuario.getDireccion(), usuario.getMail(), usuario.getTipousuario().getNombre());
		return Response.ok().entity("{\"message\":\"Modificacion Usuario exitosa\"}").build();
				
	}catch (Exception e) 
	{
		e.printStackTrace();
		return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\": \"Error al querer modificar datos del usuarios.\"}").build();
	}
	}
	
	//Eliminar Usuario
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response deleteUsuario(@PathParam("id") Long id) {
		try {
			Usuario usuario = usuarioBeanRemote.obtenerUsuarioPorId(id);
			usuarioBeanRemote.ModificarUsuario(usuario.getId(), usuario.getPass(), usuario.getUsuario(), usuario.getNombre(), 
					usuario.getApellido(), "INACTIVO", usuario.getTipodoc(), usuario.getNumerodoc(), 
					usuario.getDireccion(), usuario.getMail(), usuario.getTipousuario().getNombre());
			return Response.ok().entity("{\"message\":\"Eliminacion de Usuario exitosa\"}").build();
					
		}catch (Exception e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\": \"Error al querer Eliminar el usuario:\" usuario.getNombre()}").build();
		}
		
		
}
}
