/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.events;

import java.util.ArrayList;

import org.broulf.admin.event.Event;
import org.broulf.admin.event.Listener;

public interface HitResultRayTraceListener extends Listener
{
	public void onHitResultRayTrace(float float_1);
	
	public static class HitResultRayTraceEvent
		extends Event<HitResultRayTraceListener>
	{
		private float float_1;
		
		public HitResultRayTraceEvent(float float_1)
		{
			this.float_1 = float_1;
		}
		
		@Override
		public void fire(ArrayList<HitResultRayTraceListener> listeners)
		{
			for(HitResultRayTraceListener listener : listeners)
				listener.onHitResultRayTrace(float_1);
		}
		
		@Override
		public Class<HitResultRayTraceListener> getListenerType()
		{
			return HitResultRayTraceListener.class;
		}
	}
}
