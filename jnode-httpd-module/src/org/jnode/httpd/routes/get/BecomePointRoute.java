package org.jnode.httpd.routes.get;

import jnode.ftn.types.FtnAddress;
import jnode.main.MainHandler;

import org.jnode.httpd.util.HTML;

import spark.Request;
import spark.Response;
import spark.Route;

public class BecomePointRoute extends Route {
	private static String requestPoint = null;

	public BecomePointRoute() {
		super("/requestpoint.html");
		if (requestPoint == null) {
			requestPoint = HTML.getContents("/parts/requestpoint.html");
		}
	}

	@Override
	public Object handle(Request arg0, Response arg1) {
		StringBuilder sb = new StringBuilder();
		for (FtnAddress a : MainHandler.getCurrentInstance().getInfo()
				.getAddressList()) {
			sb.append("<option>" + a.toString() + "</option>");
		}
		return HTML.start(false)
				.append(String.format(requestPoint, sb.toString())).footer()
				.get();
	}

}
