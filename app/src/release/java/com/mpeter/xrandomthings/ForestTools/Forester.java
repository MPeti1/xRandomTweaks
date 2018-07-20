package com.mpeter.xrandomtweaks.foresttools;

import com.mpeter.xrandomtweaks.foresttools.Trees.DeadTree;

import timber.log.Timber;

public class Forester {
    public static void init() {
        Timber.plant(new DeadTree());
    }
}
