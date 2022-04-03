package pt.unl.fct.di.adc.firstwebapp.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Value;

@WebServlet("/register"
// "/register"
)

@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

/**
 * Criar um serviço REST que permita registar um novo utilizador no sistema O
 * endpoint deve ser /rest/register/v2 Recebe input em JSON com propriedades
 * username, password e confirmation, email e name Verifica se todos os valores
 * fazem sentido e estão preenchidos Verifica que utilizador não existe (ver
 * operação get da Cloud Datastore)
 *
 */
public class RegisterResource extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public RegisterResource() {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String password = request.getParameter("psw");
		String passwordCheck = request.getParameter("psw-repeat");
		String userProfile = request.getParameter("profile");
		String homePhone = request.getParameter("home-phone");
		String mobilePhone = request.getParameter("mobile-phone");
		String addr = request.getParameter("addr");
		String nif = request.getParameter("nif");
		String role = request.getParameter("role");
		String status = request.getParameter("status");

		System.out.println(role);
		LOG.fine("Attempt to register user: " + username);

		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		Entity userCheck = datastore.get(userKey);

		if (userCheck != null)

			response.sendRedirect("/userExists.html");

		else {

			if (!password.equals(passwordCheck)) {

				response.sendRedirect("/unmatchedPassword.html");

				Response.status(Status.BAD_REQUEST).entity("Passwords don't match.").build();

			}

			else {

				Entity user = Entity.newBuilder(userKey).set("user_name", name).set("user_role", role)
						.set("user_status", status).set("user_profile", userProfile)
						.set("user_pwd", DigestUtils.sha512Hex(password)).set("user_pwd_check", passwordCheck)
						.set("user_email", email).set("user_creation_time", Timestamp.now())
						.set("home_phone", homePhone).set("mobile_phone", mobilePhone).set("addr", addr).set("nif", nif)
						.build();
				
				System.out.println(password);
				System.out.println(DigestUtils.sha512Hex(password));
				
				datastore.add(user);

				if (status.equals("active")) {

					if (role.equals("user"))

						response.sendRedirect("/menuUser.html");

					else if (role.equals("superuser"))

						response.sendRedirect("/menuSuperuser.html");

					else if (role.equals("gbo"))

						response.sendRedirect("/menuGbo.html");

					else if (role.equals("gs"))

						response.sendRedirect("/menuGs.html");
				}

				else

					response.sendRedirect("/menuInactive.html");

			}
		}
	}
}
