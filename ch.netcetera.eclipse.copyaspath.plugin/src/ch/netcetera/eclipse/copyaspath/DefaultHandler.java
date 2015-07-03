/*
 * Copyright (c) 2013 Netcetera AG.
 * All rights reserved.
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * - Netcetera AG: initial implementation
 */
package ch.netcetera.eclipse.copyaspath;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handles all calls to "Copy as Path".
 */
public class DefaultHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IResource resource = null;
    ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
    Object firstElement = null;
    if (currentSelection instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) currentSelection;
      firstElement = ssel.getFirstElement();
    }
    if (firstElement instanceof IAdaptable) {
      IAdaptable adaptableFirstElement = (IAdaptable) firstElement;
      resource = (IResource) adaptableFirstElement.getAdapter(IResource.class);
    }

    if (resource == null) {
      IEditorInput activeEditorInput = HandlerUtil.getActiveEditorInput(event);
      if (activeEditorInput != null) {
        resource = (IResource) activeEditorInput.getAdapter(IResource.class);
      }
    }

    String path = null;

    if (resource != null) {
      path = resource.getLocation().toOSString();
    } else if (firstElement instanceof IJavaElement) {
      path = ((IJavaElement) firstElement).getPath().toOSString();
    }
    Clipboard clipboard = new Clipboard(HandlerUtil.getActiveShell(event).getDisplay());
    try {
      clipboard.setContents(new Object[]{path}, new Transfer[]{TextTransfer.getInstance()});
    } finally {
      clipboard.dispose();
    }
    return null;
  }
}
