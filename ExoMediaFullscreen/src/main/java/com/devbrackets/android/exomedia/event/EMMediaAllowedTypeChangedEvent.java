/*
 * Copyright (C) 2016 Brian Wernick
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devbrackets.android.exomedia.event;

import com.devbrackets.android.exomedia.manager.EMPlaylistManager;
/**
 * @deprecated EventBus support will be removed in the next major release (3.0).
 * Instead the standard listeners should be used
 */
@Deprecated
public class EMMediaAllowedTypeChangedEvent {
    private final EMPlaylistManager.MediaType allowedType;

    public EMMediaAllowedTypeChangedEvent(EMPlaylistManager.MediaType allowedType) {
        this.allowedType = allowedType;
    }

    public EMPlaylistManager.MediaType getAllowedType() {
        return allowedType;
    }
}
