package com.devbrackets.android.exomedia.event;

/**
 * Used to capture when the Fullsceen button is clicked.  This
 * can also be accessed through the callbacks in {@link com.devbrackets.android.exomedia.listener.EMVideoViewControlsCallback}
 * however unlike the callback this cannot override the default functionality. Additionally, if the callbacks
 * are implemented and consume the event, this will NOT be called.
 *
 * @deprecated EventBus support will be removed in the next major release (3.0).
 * Instead the standard listeners should be used
 */
public class EMMediaFullsceenEvent {
    //Purposefully left blank
}
