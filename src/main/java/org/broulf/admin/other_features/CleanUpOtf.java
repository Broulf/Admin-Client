/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.other_features;

import org.broulf.admin.DontBlock;
import org.broulf.admin.SearchTags;
import org.broulf.admin.other_feature.OtherFeature;

@SearchTags({"Clean Up"})
@DontBlock
public final class CleanUpOtf extends OtherFeature
{
	public CleanUpOtf()
	{
		super("CleanUp",
			"Cleans up your server list.\n"
				+ "To use it, press the 'Clean Up' button\n"
				+ "on the server selection screen.");
	}
}
