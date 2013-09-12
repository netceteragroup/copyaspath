/*
 * Copyright (c) 2009 Netcetera AG and others.
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
    IResource res = null;
    ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
    IAdaptable firstElement = null;
    if (currentSelection instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) currentSelection;
      firstElement = (IAdaptable) ssel.getFirstElement();
    }
    if (firstElement != null) {
      res = (IResource) firstElement.getAdapter(IResource.class);
    }

    if (res == null) {
      IEditorInput activeEditorInput = HandlerUtil.getActiveEditorInput(event);
      if (activeEditorInput != null) {
        res = (IResource) activeEditorInput.getAdapter(IResource.class);
      }
    }

    String path = null;

    if (res != null) {
      path = res.getLocation().toOSString();
    } else if (firstElement instanceof IJavaElement) {
      path = ((IJavaElement) firstElement).getPath().toOSString();
    }
    Clipboard clipboard = new Clipboard(HandlerUtil.getActiveShell(event).getDisplay());
    clipboard.setContents(new Object[]{path}, new Transfer[]{TextTransfer.getInstance()});
    clipboard.dispose();
    return null;
  }
}
