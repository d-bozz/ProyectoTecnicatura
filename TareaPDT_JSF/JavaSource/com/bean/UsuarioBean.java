package com.bean;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.Remote.TIpoUsuarioBeanRemote;
import com.Remote.UsuarioBeanRemote;
import com.entidades.Localidad;
import com.entidades.Observacion;
import com.entidades.TipoUsuario;
import com.entidades.Usuario;
import com.exception.ServiciosException;

@ManagedBean(name="usuario")
@SessionScoped
public class UsuarioBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB
	private UsuarioBeanRemote usuarioBeanRemote;

	@EJB
	private TIpoUsuarioBeanRemote tipousuarioBeanRemote;
	
	public UsuarioBean() {
	}

	private long id;
	private String pass;
	private String username;
	private String nombre;
	private String apellido;
	private String estado;
	private String tipodoc;
	private String numerodoc;
	private String direccion;
	private String mail;
	private String tipoUsuario;
	private List<Usuario> usuariosSeleccionados=new ArrayList<Usuario>();
	private List<Usuario> usuarioFiltrados=new ArrayList<Usuario>();

	private Usuario usuarioSeleccionado = new Usuario();
	private boolean modo1 = false;
	private boolean altaExitoso = false;
	private List<TipoUsuario> tiposUsuarios = new ArrayList<TipoUsuario>();

	public UsuarioBean( String pass, String usuario, String nombre, String apellido, String estado, String tipodoc,
			String numerodoc, String direccion, String mail, String tipoUsuario) {
		super();

		this.pass = pass;
		this.username = usuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.estado = estado;
		this.tipodoc = tipodoc;
		this.numerodoc = numerodoc;
		this.direccion = direccion;
		this.mail = mail;
		this.tipoUsuario = tipoUsuario;
	}

	public List<Usuario> getUsuarioFiltrados() {
		return usuarioFiltrados;
	}

	public void setUsuarioFiltrados(List<Usuario> usuarioFiltrados) {
		this.usuarioFiltrados = usuarioFiltrados;
	}

	
	public List<TipoUsuario> getTiposUsuarios() {
		return tiposUsuarios;
	}

	public void setTiposUsuarios(List<TipoUsuario> tiposUsuarios) {
		this.tiposUsuarios = tiposUsuarios;
	}

	public String getTipousuario() {
		return tipoUsuario;
	}
	public void setTipousuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getUsuario() {
		return username;
	}
	public void setUsuario(String usuario) {
		this.username = usuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipodoc() {
		return tipodoc;
	}
	public void setTipodoc(String tipodoc) {
		this.tipodoc = tipodoc;
	}
	public String getNumerodoc() {
		return numerodoc;
	}
	public void setNumerodoc(String numerodoc) {
		this.numerodoc = numerodoc;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public List<Usuario> getUsuariosSeleccionados() {
		return usuariosSeleccionados;
	}

	public void setUsuariosSeleccionados(List<Usuario> usuariosSeleccionados) {
		this.usuariosSeleccionados = usuariosSeleccionados;
	}

	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
	
	public boolean isNumeric (String num) {
		boolean resultado;
        try {
            Integer.parseInt(num);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
	}
	
	public void crearUsuario() {
		try{
			
			boolean validarUsuario = ValidarUsuario();
			boolean esNumerico = isNumeric(numerodoc);
			boolean bandera = true;
			String usuMayus = username.toUpperCase();
			
			try {
				
			} catch (Exception e){
				
			}
			
			if (nombre.trim().isEmpty()) {
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Debe ingresar un nombre", "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				bandera = false;
			}
			
			else if (pass.length()<8) {
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"La password no puede ser menor que 8 caracteres", "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				bandera = false;
			}
			
			else if (!(usuarioBeanRemote.existeUsuario(usuMayus).size() == 0)) {
				
				if(validarUsuario == true && usuarioBeanRemote.obtenerUsuario(usuMayus).getEstado().equals("ACTIVO")) {
					
					FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"El usuario ya existe en el sistema ", "");
					FacesContext.getCurrentInstance().addMessage(null, facesMsg);
					bandera = false;
				}	
	 		}
			else {
				
				if (tipodoc.equals("CI")) 
				{ 
					if (numerodoc.length()<7 || numerodoc.length()>8)
					{
						FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"La CI debe contener entre 7 y 8 d�gitos", "");
						FacesContext.getCurrentInstance().addMessage(null, facesMsg);
						bandera = false;
					}
					
					else if (esNumerico != true)
					{
						FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"La CI debe contener formato num�rico", "");
						FacesContext.getCurrentInstance().addMessage(null, facesMsg);
						bandera = false;
					}
				}
			}
			
			if (bandera == true)
			{
				 if (validarUsuario == true && usuarioBeanRemote.obtenerUsuario(usuMayus).getEstado().equals("INACTIVO")) 
				 {
					this.estado = "ACTIVO";
					System.out.println("El id es :" + usuarioBeanRemote.obtenerUsuario(usuMayus).getId());
					usuarioBeanRemote.ModificarUsuario(usuarioBeanRemote.obtenerUsuario(usuMayus).getId(), pass, usuMayus, nombre, apellido, estado, tipodoc, numerodoc, direccion, mail, tipoUsuario);
					//mensaje de actualizacion correcta
					FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Se reactiv� el usuario en el sistema.", "");
					FacesContext.getCurrentInstance().addMessage(null, facesMsg);
					limpiar();
					seleccionarUsuarios();
				 }
				else 
				{
					this.estado = "ACTIVO";
					usuarioBeanRemote.CrearUsuario(pass, usuMayus, nombre, apellido, estado, tipodoc, numerodoc, direccion, mail, tipoUsuario);
					altaExitoso = true;
					//mensaje de actualizacion correcta
					FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Se cre� el usuario correctamente ", "");
					FacesContext.getCurrentInstance().addMessage(null, facesMsg);
					limpiar();
					seleccionarUsuarios();
				}
			}
			
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	public boolean ValidarUsuario () {
		boolean bandera = false;	
		try {
		List<Usuario> usuarioValido = usuarioBeanRemote.existeUsuario(username);
		
		if (usuarioValido.size() != 0) {
				bandera = true;
 		}
		}
		catch (Exception ex)
		{
			ex.getMessage();;
		}
		return bandera;
	}

	public void actualizarUsuario(Usuario usuario){
		try{
			String pTipo = usuario.getTipodoc();
			String pNumero = usuario.getNumerodoc();
			boolean esNumerico = isNumeric(pNumero);
			boolean bandera = true;
			
			if (usuario.getPass().length()<8) {
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"La password no puede ser menor que 8 caracteres", "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				bandera = false;
	 		}
			
			else if (pTipo.equals("CI")) 
			{ 
				if (pNumero.length()<7 || pNumero.length()>8)
				{
					FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"La CI debe contener entre 7 y 8 d�gitos", "");
					FacesContext.getCurrentInstance().addMessage(null, facesMsg);
					bandera = false;
				}
				else if (esNumerico == false)
				{
					FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"La CI debe contener formato num�rico", "");
					FacesContext.getCurrentInstance().addMessage(null, facesMsg);
					bandera = false;
				}
			}
			
			if (bandera == true)
			{
				usuarioBeanRemote.ModificarUsuario(usuario.getId(), usuario.getPass(), usuario.getUsuario(), usuario.getNombre(), 
						usuario.getApellido(), usuario.getEstado(), usuario.getTipodoc(), usuario.getNumerodoc(), 
						usuario.getDireccion(), usuario.getMail(), usuario.getTipousuario().getNombre());
				
				//mensaje de actualizacion correcta
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Se actualiz� el usuario. ", "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				seleccionarUsuarios();
				}	
			}
			catch(Exception e){
				e.getMessage();
		}
	}

	public String darDeBajaUsuario(Usuario usuario, String usuActual) {
		try {
			Usuario usuLogueado = usuarioBeanRemote.obtenerUsuario(usuActual);
			
			if (usuLogueado.getUsuario().equals(usuario.getUsuario()))
			{
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"No se puede eliminar a usted mismo ", "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				return "No se puede eliminar a usted mismo";
			}
			
			else {
			this.estado = "INACTIVO";
			usuarioBeanRemote.ModificarUsuario(usuario.getId(), usuario.getPass(), usuario.getUsuario(), usuario.getNombre(), 
					usuario.getApellido(), this.estado, usuario.getTipodoc(), usuario.getNumerodoc(), 
					usuario.getDireccion(), usuario.getMail(), usuario.getTipousuario().getNombre());
			
			  List<Usuario> usuariosAux = usuarioBeanRemote.obtenerUsuarioActivos();
			  usuariosSeleccionados = usuariosAux;
			
				//mensaje de actualizacion correcta
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Se elimin� el usuario. ", "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				return "GestionUsuarios.xhtml";
		} 
		}catch(Exception e) {
			e.getMessage();
			return "No se puede dar de baja el usuario";
		}

	}

	public List<Usuario> obtenerUsuarios() {
		try {
			return usuarioBeanRemote.obtenerUsuarioActivos();
		} catch(Exception e){
			return null;
		}
	}


	public void preRenderViewListener() {
		modo1=true;
		if (id != 0) {
			usuarioSeleccionado = usuarioBeanRemote.obtenerUsuarioPorId(id); 
		} 
	}


	public void actionAlta(ActionEvent event) throws AbortProcessingException {
		System.out.println("Alta exitosa!");	
	}
	public void info() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Alta exitosa."));
	}

	
	
	  @PostConstruct 
	    public void init(){ 
		  usuariosSeleccionados=usuarioBeanRemote.obtenerUsuarioActivos();
		  tiposUsuarios = tipousuarioBeanRemote.obtenerTodoslosTipos();
		  usuarioFiltrados= usuarioBeanRemote.obtenerUsuarios();
		  
	  }
	

	public void limpiar(){

	    //Limpiara los objetos o propiedades
		pass = "";
		username = "";
		nombre = "";
		apellido = "";
		estado = "";
		tipodoc = "";
		numerodoc = "";
		direccion = "";
		mail = "";
		tipoUsuario = "";
	  }

	public List<Usuario> seleccionUsuario(String username)
	{
		try {
		usuariosSeleccionados=usuarioBeanRemote.obtenerUsuarioActivos();
		List <Usuario> filtradas = new ArrayList<Usuario>();
		for (Usuario u : usuariosSeleccionados) {
			
			if ((username==null || username.isEmpty() || u.getUsuario().toLowerCase().startsWith(username.toLowerCase())) &&
			     (nombre==null || nombre.isEmpty() || u.getNombre().toLowerCase().startsWith(nombre.toLowerCase())))	
			{
				
				filtradas.add(u);
				}
			
		}
		return filtradas;
		
	}catch (Exception ex) 
		{	
			ex.getMessage();
			return null;
		}
	}
	

	
	public String seleccionarUsuarios() {
		usuariosSeleccionados = seleccionUsuario(username); 
		return "";
	}
	
	
	public boolean VerificarTipo(String tipoEnviado) {
		boolean tipo = true;
		Usuario tipoUsu = usuarioBeanRemote.obtenerUsuario(tipoEnviado);
		if ((tipoUsu.getTipousuario().getNombre() == "ADMINISTRADOR" || tipoUsu.getTipousuario().getId() == 1))		
		{
			tipo = false;
		}
		return tipo;
	}
}


