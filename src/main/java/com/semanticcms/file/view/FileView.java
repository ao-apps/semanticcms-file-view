/*
 * semanticcms-file-view - SemanticCMS view of all files in the current page and all children.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2019, 2020  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of semanticcms-file-view.
 *
 * semanticcms-file-view is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * semanticcms-file-view is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with semanticcms-file-view.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.semanticcms.file.view;

import com.aoindustries.html.Html;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.renderer.html.ElementFilterTree;
import com.semanticcms.core.renderer.html.HtmlRenderer;
import com.semanticcms.core.renderer.html.View;
import com.semanticcms.file.model.File;
import com.semanticcms.file.renderer.html.FileUtils;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.SkipPageException;

/**
 * View of all files in the current page and all children.
 */
public class FileView extends View {

	public static final String NAME = "files";

	@WebListener("Registers the \"" + NAME + "\" view in HtmlRenderer.")
	public static class Initializer implements ServletContextListener {
		@Override
		public void contextInitialized(ServletContextEvent event) {
			HtmlRenderer.getInstance(event.getServletContext()).addView(new FileView());
		}
		@Override
		public void contextDestroyed(ServletContextEvent event) {
			// Do nothing
		}
	}

	private FileView() {}

	@Override
	public Group getGroup() {
		return Group.VARIABLE;
	}

	@Override
	public String getDisplay() {
		return "Files";
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isApplicable(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Page page) throws ServletException, IOException {
		return FileUtils.hasFile(servletContext, request, response, page, true);
	}

	@Override
	public String getDescription(Page page) {
		return null;
	}

	@Override
	public String getKeywords(Page page) {
		return null;
	}

	/**
	 * This view does not provide additional information unobtainable from source content,
	 * exclude from search indexes.
	 */
	@Override
	public boolean getAllowRobots(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Page page) {
		return false;
	}

	@Override
	public void doView(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Html html, Page page) throws ServletException, IOException, SkipPageException {
		html.out.write("<h1>Files in ");
		html.text(page.getTitle());
		html.out.write("</h1>\n");
		
		ElementFilterTree.writeElementFilterTreeImpl(
			servletContext,
			request,
			response,
			html,
			File.class,
			page,
			true
		);
	}
}
