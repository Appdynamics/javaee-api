/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package javax.enterprise.concurrent;

import java.util.concurrent.Future;

/**
 * A ManagedTaskListener is used to monitor the state of a task's Future.
 * It can be registered with a {@link ManagedExecutorService} using the
 * <code>submit</code> methods and will be invoked when the state of the 
 * {@link Future} changes.
 * Each listener method will run with unspecified context.
 * All listeners run without an explicit transaction 
 * (they do not enlist in the application component's transaction).  If a transaction is required, use a
 * {@link javax.transaction.UserTransaction} instance.
 * <p>
 *
 * Each listener instance will be invoked within the same process in which the listener was registered.
 * If a single listener is submitted to multiple ManagedExecutorService instances, the
 * listener object may be invoked concurrently by multiple threads.<p>
 *
 * Each listener method supports a minimum quality of service of at-most-once.  A listener is not
 * guaranteed to be invoked due to a process failure or termination.
 * <p>
 * <b>State Transition Diagram</b>
 * <p>
 * The following state transition figure and tables describe
 * the possible task lifecycle events that can occur when a
 * ManagedTaskListener is associated with a task.  Each method is invoked
 * when the state of the {@link Future} moves from one state to another.
 * <p>
 * <img src="doc-files/TaskListener_StateDiagram.gif"><p>
 *
 * <b>A. The task runs normally:</b>
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 *
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2.</td><td valign="top">Submitted</td><td valign="top">About to call run()</td><td valign="top">taskStarting</td><td valign="top">Started</td></tr>
 * <tr><td valign="top">3.</td><td valign="top">Started</td><td valign="top">Exit run()</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 *
 * </table><p>
 *
 * <b>B. The task is cancelled during taskSubmitted():</b>
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted<br>Future is cancelled.</td><td valign="top">Cancelling</td></tr>
 *
 * <tr><td valign="top">2.</td><td valign="top">Cancelling</td><td valign="top">&nbsp;</td><td valign="top">taskAborted</td><td valign="top">Cancelled</td></tr>
 * <tr><td valign="top">3.</td><td valign="top">Cancelled</td><td valign="top">&nbsp;</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 * </table><p>
 *
 * <b>C. The task is cancelled or aborted after submitted, but before started:</b>
 *
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2.</td><td valign="top">Submitted</td><td valign="top">cancel() or abort</td><td valign="top">taskAborted</td><td valign="top">Cancelled</td></tr>
 *
 * <tr><td valign="top">3.</td><td valign="top">Cancelled</td><td valign="top">&nbsp;</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 * </table> <p>
 *
 * <b>D. The task is cancelled when it is starting:</b>
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 *
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2.</td><td valign="top">Submitted</td><td valign="top">About to call run()</td><td valign="top">taskStarting<br>Future is cancelled.</td><td valign="top">Cancelling</td></tr>
 * <tr><td valign="top">3.</td><td valign="top">Cancelling</td><td valign="top">&nbsp;</td><td valign="top">taskAborted</td><td valign="top">Cancelled</td></tr>
 *
 * <tr><td valign="top">4.</td><td valign="top">Cancelled</td><td valign="top">&nbsp;</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 * </table>
 * <P>
 * 
 * @since 1.0
 */
public interface ManagedTaskListener {

  /**
   * Called after the task has been submitted to the Executor. The task will not enter the 
   * starting state until the taskSubmitted listener has completed. 
   * This method may be called from the same thread that the task was submitted with.
   * <p>
   * This event does not indicate that the task has been scheduled for execution.
   *  
   * @param future the <code>Future</code> instance that was created when the task was submitted. 
   * @param executor the executor used to run the associated Future.
   * @param task the task that was submitted.
   */
  public void taskSubmitted(java.util.concurrent.Future<?> future,
                            ManagedExecutorService executor,
                            Object task);
  
  /**
   * Called when a task's Future has been cancelled anytime during the life of a task.
   * This method may be called after taskDone(). The {@link Future#isCancelled()} 
   * method returns false if the task was aborted through another means other than 
   * {@link Future#cancel(boolean) }. 
   * The exception argument will represent the cause of the cancellation:
   * <ul>
   * <li>{@link java.util.concurrent.CancellationException} if the task was cancelled,
   * <li>{@link SkippedException} if the task was skipped or
   * <li>{@link AbortedException} if the task failed to start for another reason. 
   * </ul>
   * The <code>AbortedException.getCause()</code> method will return the exception that 
   * caused the task to fail to start. 
   * 
   * @param future the {@link Future} instance that was created when the task was submitted. 
   * @param executor the executor used to run the associated Future.
   * @param task the task that was submitted.
   * @param exception the cause of the task abort.
   */
  public void taskAborted(java.util.concurrent.Future<?> future,
                          ManagedExecutorService executor,
                          Object task,
                          java.lang.Throwable exception);
  
  /**
   * Called when a submitted task has completed running, either successfully or
   * failed due to any exception thrown from the task, task being cancelled,
   * rejected, or aborted.
   *  
   * @param future the {@link Future} instance that was created when the task was submitted. 
   * @param executor the executor used to run the associated Future.
   * @param task the task that was submitted.
   * @param exception if not null, the exception that caused the task to fail.
   */
  public void taskDone(java.util.concurrent.Future<?> future,
                       ManagedExecutorService executor,
                       Object task,
                       java.lang.Throwable exception);
  
  /**
   * This method is called before the task is about to start. The task will
   * not enter the starting state until the taskSubmitted listener has completed. This method 
   * may be called from the same thread that the task was submitted with.
   *  
   * @param future the {@link Future} instance that was created when the task was submitted. 
   * @param executor the executor used to run the associated Future.
   * @param task the task that was submitted.
   */
  public void taskStarting(java.util.concurrent.Future<?> future,
                           ManagedExecutorService executor,
                           Object task);
}
