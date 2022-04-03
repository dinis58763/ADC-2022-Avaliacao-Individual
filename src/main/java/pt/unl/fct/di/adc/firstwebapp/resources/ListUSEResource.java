package pt.unl.fct.di.adc.firstwebapp.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

@WebServlet("/listuser"
//"/list"
)

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class ListUSEResource extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public ListUSEResource() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Query<Entity> queryusers = Query.newEntityQueryBuilder().setKind("User")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("user_role", "user"),
						PropertyFilter.eq("user_profile", "public"), PropertyFilter.eq("user_status", "active")))
				.build();

		QueryResults<Entity> logsusers = datastore.run(queryusers);

		List<String> users = new ArrayList<>();

		logsusers.forEachRemaining(user -> {

			Key userKey = datastore.newKeyFactory().setKind("User").newKey(user.getKey().getName());
			Entity userUp = datastore.get(userKey);

			users.add("Username: " + user.getKey().getName());
			users.add("Email: " + userUp.getString("user_email"));
			users.add("Name: " + userUp.getString("user_name"));
			users.add("Status (just to check): " + userUp.getString("user_status"));
			users.add("Profile (just to check): " + userUp.getString("user_profile"));
			users.add("");
		});

		PrintWriter out = response.getWriter();

		out.println("USER");
		out.println("");

		for (int i = 0; i < users.size(); i++)

			out.println(users.get(i));

	}
}
