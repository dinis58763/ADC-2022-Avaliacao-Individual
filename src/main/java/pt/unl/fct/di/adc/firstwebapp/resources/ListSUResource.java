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

@WebServlet("/listsuperuser"
//"/list"
)

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class ListSUResource extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private final Gson g = new Gson();
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	// KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

	public ListSUResource() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		LOG.fine("Attempt to list users");

		Query<Entity> querysuperusers = Query.newEntityQueryBuilder().setKind("User")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("user_role", "superuser"))).build();

		QueryResults<Entity> logsuperusers = datastore.run(querysuperusers);

		Query<Entity> querygs = Query.newEntityQueryBuilder().setKind("User")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("user_role", "gs"))).build();

		QueryResults<Entity> logsgs = datastore.run(querygs);

		Query<Entity> querygbos = Query.newEntityQueryBuilder().setKind("User")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("user_role", "gbo"))).build();

		QueryResults<Entity> logsgbos = datastore.run(querygbos);

		Query<Entity> queryusers = Query.newEntityQueryBuilder().setKind("User")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("user_role", "user"))).build();

		QueryResults<Entity> logsusers = datastore.run(queryusers);

		List<String> superusers = new ArrayList<>();
		List<String> gs = new ArrayList<>();
		List<String> gbos = new ArrayList<>();
		List<String> users = new ArrayList<>();

		logsuperusers.forEachRemaining(user -> { // list all attributes

			Key userKey = datastore.newKeyFactory().setKind("User").newKey(user.getKey().getName());
			Entity userUp = datastore.get(userKey);

			superusers.add("Username: " + user.getKey().getName());
			superusers.add("Email: " + userUp.getString("user_email"));
			superusers.add("Name: " + userUp.getString("user_name"));
			superusers.add("Hashed Password: " + userUp.getString("user_pwd"));

			if ((userUp.getString("home_phone") != ""))
				superusers.add("Home Phone: " + userUp.getString("home_phone"));
			else
				superusers.add("Home Phone Not Found");

			if (userUp.getString("mobile_phone") != "")
				superusers.add("Mobile Phone: " + userUp.getString("mobile_phone"));
			else
				superusers.add("Mobile Phone Not Found");

			if (userUp.getString("addr") != "")
				superusers.add("Address: " + userUp.getString("addr"));
			else
				superusers.add("Address Not Found");

			if (userUp.getString("nif") != "")
				superusers.add("NIF: " + userUp.getString("nif"));
			else
				superusers.add("NIF Not Found");

			superusers.add("Role: " + userUp.getString("user_role"));
			superusers.add("Status: " + userUp.getString("user_status"));
			superusers.add("Profile: " + userUp.getString("user_profile"));
			superusers.add("");
		});

		logsgs.forEachRemaining(user -> { // list all attributes

			Key userKey = datastore.newKeyFactory().setKind("User").newKey(user.getKey().getName());
			Entity userUp = datastore.get(userKey);

			gs.add("Username: " + user.getKey().getName());
			gs.add("Email: " + userUp.getString("user_email"));
			gs.add("Name: " + userUp.getString("user_name"));
			gs.add("Hashed Password: " + userUp.getString("user_pwd"));

			if ((userUp.getString("home_phone") != ""))
				gs.add("Home Phone: " + userUp.getString("home_phone"));
			else
				gs.add("Home Phone Not Found");

			if (userUp.getString("mobile_phone") != "")
				gs.add("Mobile Phone: " + userUp.getString("mobile_phone"));
			else
				gs.add("Mobile Phone Not Found");

			if (userUp.getString("addr") != "")
				gs.add("Address: " + userUp.getString("addr"));
			else
				gs.add("Address Not Found");

			if (userUp.getString("nif") != "")
				gs.add("NIF: " + userUp.getString("nif"));
			else
				gs.add("NIF Not Found");

			gs.add("Role: " + userUp.getString("user_role"));
			gs.add("Status: " + userUp.getString("user_status"));
			gs.add("Profile: " + userUp.getString("user_profile"));
			gs.add("");
		});

		logsgbos.forEachRemaining(user -> { // list all attributes

			Key userKey = datastore.newKeyFactory().setKind("User").newKey(user.getKey().getName());
			Entity userUp = datastore.get(userKey);

			gbos.add("Username: " + user.getKey().getName());
			gbos.add("Email: " + userUp.getString("user_email"));
			gbos.add("Name: " + userUp.getString("user_name"));
			gbos.add("Hashed Password: " + userUp.getString("user_pwd"));

			if ((userUp.getString("home_phone") != ""))
				gbos.add("Home Phone: " + userUp.getString("home_phone"));
			else
				gbos.add("Home Phone Not Found");

			if (userUp.getString("mobile_phone") != "")
				gbos.add("Mobile Phone: " + userUp.getString("mobile_phone"));
			else
				gbos.add("Mobile Phone Not Found");

			if (userUp.getString("addr") != "")
				gbos.add("Address: " + userUp.getString("addr"));
			else
				gbos.add("Address Not Found");

			if (userUp.getString("nif") != "")
				gbos.add("NIF: " + userUp.getString("nif"));
			else
				gbos.add("NIF Not Found");

			gbos.add("Role: " + userUp.getString("user_role"));
			gbos.add("Status: " + userUp.getString("user_status"));
			gbos.add("Profile: " + userUp.getString("user_profile"));
			gbos.add("");
		});

		logsusers.forEachRemaining(user -> { // list all attributes

			Key userKey = datastore.newKeyFactory().setKind("User").newKey(user.getKey().getName());
			Entity userUp = datastore.get(userKey);

			users.add("Username: " + user.getKey().getName());
			users.add("Email: " + userUp.getString("user_email"));
			users.add("Name: " + userUp.getString("user_name"));
			users.add("Hashed Password: " + userUp.getString("user_pwd"));

			if ((userUp.getString("home_phone") != ""))
				users.add("Home Phone: " + userUp.getString("home_phone"));
			else
				users.add("Home Phone Not Found");

			if (userUp.getString("mobile_phone") != "")
				users.add("Mobile Phone: " + userUp.getString("mobile_phone"));
			else
				users.add("Mobile Phone Not Found");

			if (userUp.getString("addr") != "")
				users.add("Address: " + userUp.getString("addr"));
			else
				users.add("Address Not Found");

			if (userUp.getString("nif") != "")
				users.add("NIF: " + userUp.getString("nif"));
			else
				users.add("NIF Not Found");

			users.add("Role: " + userUp.getString("user_role"));
			users.add("Status: " + userUp.getString("user_status"));
			users.add("Profile: " + userUp.getString("user_profile"));
			users.add("");
		});

		PrintWriter out = response.getWriter();

		out.println("SUPERUSER");
		out.println("");

		for (int i = 0; i < superusers.size(); i++)

			out.println(superusers.get(i));

		out.println("");
		out.println("GS");
		out.println("");

		for (int i = 0; i < gs.size(); i++)

			out.println(gs.get(i));

		out.println("");
		out.println("GBO");
		out.println("");

		for (int i = 0; i < gbos.size(); i++)

			out.println(gbos.get(i));

		out.println("");
		out.println("USER");
		out.println("");

		for (int i = 0; i < users.size(); i++)

			out.println(users.get(i));
	}
}