/*
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen;

import java.util.Date;

/**
 * Provides app information.
 *
 * @user jens
 * @date 2011-10-04 09:55
 */
public interface Info {
    String getName();

    String getVersion();

    Date getBuildDate();
}
