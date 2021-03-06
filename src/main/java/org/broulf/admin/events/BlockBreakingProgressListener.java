/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.events;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.broulf.admin.event.Event;
import org.broulf.admin.event.Listener;

public interface BlockBreakingProgressListener extends Listener
{
	public void onBlockBreakingProgress(BlockBreakingProgressEvent event);
	
	public static class BlockBreakingProgressEvent
		extends Event<BlockBreakingProgressListener>
	{
		private final BlockPos blockPos;
		private final Direction direction;
		
		public BlockBreakingProgressEvent(BlockPos blockPos,
			Direction direction)
		{
			this.blockPos = blockPos;
			this.direction = direction;
		}
		
		@Override
		public void fire(ArrayList<BlockBreakingProgressListener> listeners)
		{
			for(BlockBreakingProgressListener listener : listeners)
				listener.onBlockBreakingProgress(this);
		}
		
		@Override
		public Class<BlockBreakingProgressListener> getListenerType()
		{
			return BlockBreakingProgressListener.class;
		}
		
		public BlockPos getBlockPos()
		{
			return blockPos;
		}
		
		public Direction getDirection()
		{
			return direction;
		}
	}
}
