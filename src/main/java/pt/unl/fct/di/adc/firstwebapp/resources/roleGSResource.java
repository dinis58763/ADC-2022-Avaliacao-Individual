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

@WebServlet("/gsmenu" // "/delete"
)

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class roleGSResource extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	
	String nameUp = "";
	String emailUp = "";
	String homePhoneUp = "";
	String mobilePhoneUp = "";
	String addrUp = "";
	String nifUp = "";
	String roleUp = "";
	String statusUp = "";
	String profileUp = "";
	String passwordUp = "";
	String passwordCheckUp = "";

	public roleGSResource() {
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

			String roleUser = user.getString("user_role");
			
			if (roleUser.equals("user") || roleUser.equals("gbo") || roleUser.equals("gs")) {

				passwordUp = user.getString("user_pwd");
				passwordCheckUp = user.getString("user_pwd_check");

				if (name != null)
					nameUp = name;
				else
					nameUp = user.getString("user_name");

				if (email != null)
					emailUp = email;
				else
					emailUp = user.getString("user_email");

				if (profile != null)
					profileUp = profile;
				else
					profileUp = user.getString("user_profile");

				if (homePhone != null)
					homePhoneUp = homePhone;
				else
					homePhoneUp = user.getString("home_phone");

				if (mobilePhone != null)
					mobilePhoneUp = mobilePhone;
				else
					mobilePhoneUp = user.getString("mobile_phone");	
				
				if (addr != null)
					addrUp = addr;
				else
					addrUp = user.getString("addr");

				if (nif != null)
					nifUp = nif;
				else
					nifUp = user.getString("nif");
					
				statusUp = status; // updates status
				
				if(role.equals("user") || role.equals("gbo"))
					roleUp = role; // can't update role
				else
					response.sendRedirect("/unauthenticated.html");
					
				Entity newEntity = Entity.newBuilder(userKey).set("user_name", name)
						.set("user_pwd", passwordUp).set("user_pwd_check", passwordCheckUp)
						.set("user_email", email).set("home_phone", homePhone).set("mobile_phone", mobilePhone)
						.set("addr", addr).set("nif", nif).set("user_role", role).set("user_status", status)
						.set("user_profile", profile).build();
				
				// user = entityBuilder.build();
				datastore.put(newEntity);

				response.sendRedirect("/updateSuccess.html");

			} 
			else
				response.sendRedirect("/unauthenticated.html");

		}

		else
			response.sendRedirect("/wrongUsername.html");

	}
}
