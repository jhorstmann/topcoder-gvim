package de.planetxml.topcoder;

import java.util.*;

public class VimBufferList {
    private Map bufferByName = new HashMap();
    private Map bufferById   = new HashMap();

    private int lastBufferNumber = 0;

    public VimBufferList() {
    }

    public void addBuffer(final VimBuffer buffer) {
        bufferByName.put(buffer.getName(), buffer);
        bufferById.put(Integer.valueOf(buffer.getId()), buffer);
    }

    public VimBuffer getBufferByName(final String name) {
        VimBuffer buffer = (VimBuffer)bufferByName.get(name);
        if (buffer == null) {
            buffer = new VimBuffer(++lastBufferNumber, name);
            addBuffer(buffer);
        }
        return buffer;
    }

    public VimBuffer getBufferById(final int id) {
        VimBuffer buffer = (VimBuffer)bufferById.get((Integer.valueOf(id)));
        if (buffer == null) {
            throw new IllegalArgumentException("Unknown buffer " + id);
        }
        else {
            return buffer;
        }
    }

    public void removeBufferByName(final String name) {
        VimBuffer buffer = (VimBuffer)bufferByName.remove(name);
        if (buffer == null) {
            throw new IllegalArgumentException("Unknown buffer " + name);
        }
        else {
            bufferById.remove(Integer.valueOf(buffer.getId()));
        }
    }

    public void removeBufferById(final int id) {
        VimBuffer buffer = (VimBuffer)bufferById.remove(Integer.valueOf(id));
        if (buffer == null) {
            throw new IllegalArgumentException("Unknown buffer " + id);
        }
        else {
            bufferByName.remove(buffer.getName());
        }
    }
}
