package pt.unl.fct.di.adc.firstwebapp.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

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
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

import com.google.gson.Gson;

@WebServlet("/update" // "/delete"
)

@Path("/updatePassword")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class UpdatePasswordResource extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	
	String name = "";
	String email = "";
	String homePhone = "";
	String mobilePhone = "";
	String addr = "";
	String nif = "";
	String role = "";
	String status = "";
	String profile = "";

	public UpdatePasswordResource() {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("usernameUpdate");
		String password = request.getParameter("pswUpdate");
		String newPassword = request.getParameter("psw-newUpdate");
		String checkNewPassword = request.getParameter("psw-newCheckUpdate");

		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		Entity user = datastore.get(userKey);

		if (user != null) {

			String hashedPWD = user.getString("user_pwd");
			if (hashedPWD.equals(DigestUtils.sha512Hex(password)) && (newPassword.equals(checkNewPassword))) {

				if (password.equals(newPassword) && newPassword.equals(checkNewPassword)) {
					response.sendRedirect("/samePasswords.html");
				}

				else {

					if (user.getString("user_name") != null)
						name = user.getString("user_name");
					else
						name = "";

					if (user.getString("user_email") != null)
						email = user.getString("user_email");
					else
						email = "";

					if (user.getString("home_phone") != null)
						homePhone = user.getString("home_phone");
					else
						homePhone = "";

					if (user.getString("mobile_phone") != null)
						mobilePhone = user.getString("mobile_phone");
					else
						mobilePhone = "";

					if (user.getString("addr") != null)
						addr = user.getString("addr");
					else
						addr = "";

					if (user.getString("nif") != null)
						nif = user.getString("nif");
					else
						nif = "";
					
					role = user.getString("user_role");

					status = user.getString("user_status");

					profile = user.getString("user_profile");

					Entity newEntity = Entity.newBuilder(userKey).set("user_name", name)
							.set("user_pwd", DigestUtils.sha512Hex(newPassword)).set("user_pwd_check", checkNewPassword)
							.set("user_email", email).set("home_phone", homePhone).set("mobile_phone", mobilePhone)
							.set("addr", addr).set("nif", nif).set("user_role", role).set("user_status", status)
							.set("user_profile", profile).build();
					
					// user = entityBuilder.build();
					datastore.put(newEntity);
					
					// datastore.put(user);
					response.sendRedirect("/updateSuccess.html");

				}
			}

			else
				response.sendRedirect("/wrongPassword.html");

		}

		else
			response.sendRedirect("/wrongUsername.html");

	}
}
