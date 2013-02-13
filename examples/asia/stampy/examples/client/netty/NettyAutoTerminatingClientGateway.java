/*
 * Copyright (C) 2013 Burton Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 */
package asia.stampy.examples.client.netty;

import asia.stampy.client.mina.ClientMinaMessageGateway;
import asia.stampy.client.netty.ClientNettyMessageGateway;

/**
 * Used by client examples to terminate the Java process when
 * {@link ClientMinaMessageGateway#shutdown()} is invoked.
 */
public class NettyAutoTerminatingClientGateway extends ClientNettyMessageGateway {

  /*
   * (non-Javadoc)
   * 
   * @see asia.stampy.client.mina.ClientMinaMessageGateway#shutdown()
   */
  @Override
  public void shutdown() throws Exception {
    super.shutdown();
    System.exit(0);
  }

}