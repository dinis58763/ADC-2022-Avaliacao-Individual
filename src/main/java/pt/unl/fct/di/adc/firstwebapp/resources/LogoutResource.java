package pt.unl.fct.di.adc.firstwebapp.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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
import com.google.cloud.datastore.Value;

import com.google.gson.Gson;

import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;

@WebServlet("/logout" // "/delete"
)

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class LogoutResource extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	public LogoutResource() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("usernameLogout");
		String password = request.getParameter("pswLogout");
		String checkPassword = request.getParameter("pswCheckLogout");

		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		Entity user = datastore.get(userKey);

		AuthToken authToken = new AuthToken(username);
		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(authToken.username);
		Entity token = datastore.get(tokenKey);

		if (user != null && token != null) {
			
			String hashedPWD = user.getString("user_pwd");
			
			// hashedPWD.equals(DigestUtils.sha512Hex(password))
			// user.getString("user_pwd_check").equals(checkPassword)
			// user.getString("user_pwd_check").equals(checkPassword)
			if (hashedPWD.equals(DigestUtils.sha512Hex(password))
					&&  hashedPWD.equals(DigestUtils.sha512Hex(checkPassword))) {

				datastore.delete(tokenKey);

				// Response.ok(g.toJson(token)).build();
				response.sendRedirect("/login.html");

			}

			else
				response.sendRedirect("/wrongPassword.html");

		}

		else if (user != null && token == null)
			response.sendRedirect("/loginFirst.html");

		else
			response.sendRedirect("/wrongUsername.html");
	}
}
