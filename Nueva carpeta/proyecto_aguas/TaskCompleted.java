package com.example.luisreyes.proyecto_aguas;

import org.json.JSONException;

/**
 * Created by luis.reyes on 29/08/2019.
 */

public interface TaskCompleted {
    // Define data you like to return from AysncTask
    public void onTaskComplete(String type, String result) throws JSONException;
}
