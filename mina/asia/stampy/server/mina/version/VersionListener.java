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
package asia.stampy.server.mina.version;

import java.lang.invoke.MethodHandles;

import javax.annotation.Resource;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asia.stampy.client.message.connect.ConnectHeader;
import asia.stampy.client.message.connect.ConnectMessage;
import asia.stampy.client.message.stomp.StompMessage;
import asia.stampy.common.HostPort;
import asia.stampy.common.message.StampyMessage;
import asia.stampy.common.message.StompMessageType;
import asia.stampy.common.mina.StampyMinaMessageListener;

/**
 * The listener interface for receiving version events. The class that is
 * interested in processing a version event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addVersionListener<code> method. When
 * the version event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see VersionEvent
 */
@Resource
public class VersionListener implements StampyMinaMessageListener {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static StompMessageType[] TYPES = { StompMessageType.CONNECT, StompMessageType.STOMP };

  private static final String VERSION = "1.2";

  /*
   * (non-Javadoc)
   * 
   * @see asia.stampy.common.mina.StampyMinaMessageListener#getMessageTypes()
   */
  @Override
  public StompMessageType[] getMessageTypes() {
    return TYPES;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * asia.stampy.common.mina.StampyMinaMessageListener#isForMessage(asia.stampy
   * .common.message.StampyMessage)
   */
  @Override
  public boolean isForMessage(StampyMessage<?> message) {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * asia.stampy.common.mina.StampyMinaMessageListener#messageReceived(asia.
   * stampy.common.message.StampyMessage,
   * org.apache.mina.core.session.IoSession, asia.stampy.common.HostPort)
   */
  @Override
  public void messageReceived(StampyMessage<?> message, IoSession session, HostPort hostPort) throws Exception {
    switch (message.getMessageType()) {
    case CONNECT:
      checkVersion(hostPort, ((ConnectMessage) message).getHeader());
      break;
    case STOMP:
      checkVersion(hostPort, ((StompMessage) message).getHeader());
      break;
    default:
      break;

    }
  }

  private void checkVersion(HostPort hostPort, ConnectHeader header) throws StompVersionException {
    String acceptVersion = header.getAcceptVersion();

    String[] parts = acceptVersion.split(",");

    for (String part : parts) {
      if (part.trim().equals(VERSION)) {
        log.info("Accept version is valid for {}", hostPort);
        return;
      }
    }

    throw new StompVersionException("Only STOMP version " + VERSION + " is supported");
  }

}
