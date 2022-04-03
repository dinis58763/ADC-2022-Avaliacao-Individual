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
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;

import com.google.gson.Gson;

@WebServlet("/superusermenu" // "/delete"
)

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class roleSUResource extends HttpServlet {

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
	String password = "";
	String passwordCheck = "";

	public roleSUResource() {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String profile = request.getParameter("profile");
		String homePhone = request.getParameter("home-phone");
		String mobilePhone = request.getParameter("mobile-phone");
		String addr = request.getParameter("addr");
		String nif = request.getParameter("nif");
		String status = request.getParameter("status");
		String role = request.getParameter("role");

		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		Entity user = datastore.get(userKey);
		
		if (user != null) {
			
			password = user.getString("user_pwd");
			passwordCheck = user.getString("user_pwd_check");

			if (name != null)
				this.name = name;
			else
				this.name = user.getString("user_name");

			if (email != null)
				this.email = email;
			else
				this.email = user.getString("user_email");

			if (profile != null)
				this.profile = profile;
			else
				this.profile = user.getString("user_profile");

			if (homePhone != null)
				this.homePhone = homePhone;
			else
				this.homePhone = user.getString("home_phone");

			if (mobilePhone != null)
				this.mobilePhone = mobilePhone;
			else
				this.mobilePhone = user.getString("mobile_phone");

			if (addr != null)
				this.addr = addr;
			else
				this.addr = user.getString("addr");

			if (nif != null)
				this.nif = nif;

			else
				this.nif = user.getString("nif");

			if (status != null)
				this.status = status;
			else
				this.status = user.getString("user_status");

			if (role != null)
				this.role = role;
			else
				this.role = user.getString("user_role");

			Entity.Builder entityBuilder = Entity.newBuilder(userKey);

			entityBuilder.set("user_name", this.name).set("user_role", this.role).set("user_status", this.status)
					.set("user_pwd", password).set("user_pwd_check", passwordCheck)
					.set("user_profile", this.profile).set("user_email", this.email).set("home_phone", this.homePhone)
					.set("mobile_phone", this.mobilePhone).set("addr", this.addr).set("nif", this.nif).build();

			Entity entity = entityBuilder.build();

			datastore.put(entity);

			response.sendRedirect("/updateSuccess.html");

		}

		else
			response.sendRedirect("/wrongUsername.html");

	}
}
