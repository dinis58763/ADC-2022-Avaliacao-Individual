package pt.unl.fct.di.adc.firstwebapp.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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

@WebServlet(name = "HelloAppEngine", urlPatterns = { "/login" }
//"/login"
)

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class LoginResource extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	public LoginResource() {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String username = request.getParameter("usernameLogin");
		String password = request.getParameter("pswLogin");

		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		Entity user = datastore.get(userKey);

		if (user != null) {
			String hashedPWD = user.getString("user_pwd");

			AuthToken authToken = new AuthToken(username);
			Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(authToken.username);

			if (datastore.get(tokenKey) != null)
				response.sendRedirect("/logout.html");

			else {

				if (hashedPWD.equals(DigestUtils.sha512Hex(password))) {

					Entity token = Entity.newBuilder(tokenKey).set("token_id", authToken.tokenID)
							.set("token_creation_time", authToken.creationData)
							.set("token_expiration_date", authToken.expirationData).build();

					datastore.add(token);

					if (user.getString("user_status").equals("active")) {

						if (user.getString("user_role").equals("user"))
							response.sendRedirect("/menuUser.html");

						else if (user.getString("user_role").equals("superuser"))
							response.sendRedirect("/menuSuperuser.html");

						else if (user.getString("user_role").equals("gbo"))
							response.sendRedirect("/menuGbo.html");

						else if (user.getString("user_role").equals("gs"))
							response.sendRedirect("/menuGs.html");
					} else
						response.sendRedirect("/menuInactive.html");
				} else
					response.sendRedirect("/wrongPassword.html");
			}
		} else
			response.sendRedirect("/wrongUsername.html");
	}
}
