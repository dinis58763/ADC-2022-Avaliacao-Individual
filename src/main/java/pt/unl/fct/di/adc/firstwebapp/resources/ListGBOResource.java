package pt.unl.fct.di.adc.firstwebapp.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;

import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;

@WebServlet("/listgbo"
//"/list"
)

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class ListGBOResource extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public ListGBOResource() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Query<Entity> querynew = Query.newEntityQueryBuilder().setKind("User").setFilter(
				CompositeFilter.and(PropertyFilter.eq("user_role", "user")))
				.build();

		QueryResults<Entity> logs = datastore.run(querynew);

		List<String> users = new ArrayList<>();
		
		
		logs.forEachRemaining(user -> {

			Key userKey = datastore.newKeyFactory().setKind("User").newKey(user.getKey().getName());
			Entity userUp = datastore.get(userKey);
			
			users.add("Username: " + user.getKey().getName());
			users.add("Email: " + userUp.getString("user_email"));
			users.add("Name: " + userUp.getString("user_name"));
			users.add("Hashed Password: " + userUp.getString("user_pwd"));
			
			if((userUp.getString("home_phone") != "")) 
				users.add("Home Phone: " + userUp.getString("home_phone"));
			else 
				users.add("Home Phone Not Found");
			
			if(userUp.getString("mobile_phone") != "")
				users.add("Mobile Phone: " + userUp.getString("mobile_phone"));
			else
				users.add("Mobile Phone Not Found");
			
			if(userUp.getString("addr") != "")
				users.add("Address: " + userUp.getString("addr"));
			else
				users.add("Address Not Found");
			
			if(userUp.getString("nif") != "")
				users.add("NIF: " + userUp.getString("nif"));
			else
				users.add("NIF Not Found");

			users.add("Role: " + userUp.getString("user_role"));
			users.add("Status: " + userUp.getString("user_status"));
			users.add("Profile: " + userUp.getString("user_profile"));
			users.add("");
			
			// users.add(user.getKey().getName());
			// users.add(user.getString("user_name"));
			// list all attributes
		});

		PrintWriter out = response.getWriter();

		out.println("USER");
		out.println("");
		
		for (int i = 0; i < users.size(); i++)

			out.println(users.get(i));

	}
}