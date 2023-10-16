/*
 * semanticcms-file-view - SemanticCMS view of all files in the current page and all children.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2019, 2020, 2021, 2022, 2023  AO Industries, Inc.
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
 * along with semanticcms-file-view.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.semanticcms.file.view;

import com.aoapps.html.servlet.FlowContent;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.servlet.SemanticCMS;
import com.semanticcms.core.servlet.View;
import com.semanticcms.core.servlet.impl.ElementFilterTreeImpl;
import com.semanticcms.file.model.File;
import com.semanticcms.file.servlet.FileUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings("PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES")
public final class FileView extends View {

  public static final String NAME = "files";

  /**
   * Registers the "{@link #NAME}" view in {@link SemanticCMS}.
   */
  @WebListener("Registers the \"" + NAME + "\" view in SemanticCMS.")
  public static class Initializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
      SemanticCMS.getInstance(event.getServletContext()).addView(new FileView());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
      // Do nothing
    }
  }

  private FileView() {
    // Do nothing
  }

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
  public <__ extends FlowContent<__>> void doView(
      ServletContext servletContext,
      HttpServletRequest request,
      HttpServletResponse response,
      __ flow,
      Page page
  ) throws ServletException, IOException, SkipPageException {
    flow.h1__(h1 -> h1
        .text("Files in ").text(page.getTitle())
    );
    ElementFilterTreeImpl.writeElementFilterTreeImpl(
        servletContext,
        request,
        response,
        flow,
        File.class,
        page,
        true
    );
  }
}
