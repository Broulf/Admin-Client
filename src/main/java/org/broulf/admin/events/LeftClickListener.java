/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.events;

import java.util.ArrayList;

import org.broulf.admin.event.CancellableEvent;
import org.broulf.admin.event.Listener;

public interface LeftClickListener extends Listener
{
	public void onLeftClick(LeftClickEvent event);
	
	public static class LeftClickEvent
		extends CancellableEvent<LeftClickListener>
	{
		@Override
		public void fire(ArrayList<LeftClickListener> listeners)
		{
			for(LeftClickListener listener : listeners)
			{
				listener.onLeftClick(this);
				
				if(isCancelled())
					break;
			}
		}
		
		@Override
		public Class<LeftClickListener> getListenerType()
		{
			return LeftClickListener.class;
		}
	}
}
