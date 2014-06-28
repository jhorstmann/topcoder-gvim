package de.planetxml.topcoder;

class Message {
    public static final int COMMAND   = '!';
    public static final int FUNCTION  = '/';
    public static final int EVENT     = '=';
    public static final int REPLY     = 'R';
    public static final int SPECIAL   = 'S';

    public static final String ACCEPT = "ACCEPT";
    public static final String AUTH   = "AUTH";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String DETACH = "DETACH";
    public static final String REJECT = "REJECT";

    private String args;
    private String name;
    private int    bufno = -1;
    private int    type  = -1;
    private int    seqno = -1;

    public Message(String name) {
        this.name = name;
        this.type = SPECIAL;
    }

    public Message(String name, String args) {
        this.name = name;
        this.args = args;
        this.type = SPECIAL;
    }

    public Message(int seqno, String args) {
        this.seqno = seqno;
        this.args  = args;
        this.type  = REPLY;
    }

    public Message(int bufno, String name, int type, int seqno, String args) {
        this.bufno = bufno;
        this.name  = name;
        this.type  = type;
        this.seqno = seqno;
        this.args  = args;
    }

    public String encode() {
        if (name != null) {
            return bufno + ":" + name + (char)type + seqno + (args != null ? " " + String.valueOf(args) : "") + "\n";
        }
        else {
            return seqno + (args != null ? " " + args : "") + "\n";
        }
    }

    public String encodeReply(String args) {
        return seqno + " " + args + "\n";
    }

    /*
    public String encodeReply(Long args) {
        return encodeReply(String.valueOf(args));
    }
    */

    public String getName() {
        return name;
    }

    public int getBufno() {
        return bufno;
    }

    public int getSeqno() {
        return seqno;
    }

    public int getType() {
        return type;
    }

    public String getTypeFMT() {
        if      (type == '!') return "COMMAND";
        else if (type == '/') return "FUNCTION";
        else if (type == '=') return "EVENT";
        else if (type == 'R') return "REPLY";
        else if (type == 'S') return "SPECIAL";
        else                  return "UNKNOWN";
    }

    public String getStringArgs() {
        return args;
    }

    public String getStringArgsDecoded() {
        return new Parser(args).parseString();
    }

    public int getIntArgs() {
        return Integer.parseInt(args);
    }

    public String[] getPairArgs() {
        int idx = args.indexOf(' ');
        if (idx < 0) {
            throw new IllegalStateException(args);
        }
        else {
            return new String[] {
                args.substring(0, idx),
                args.substring(idx+1)
            };
        }
    }

    private String getArgsFMT() {
        if (args != null) {
            String tmp = String.valueOf(args);
            if  (tmp.length() > 64) {
                return tmp.substring(0, 61) + "...";
            }
            else {
                return tmp;
            }
        }
        else {
            return "";
        }
    }

    public String toString() {
        return "Message" //+ Integer.toHexString(hashCode())
            + "[name=" + name
            + ", bufno=" + bufno
            + ", seqno=" + seqno
            + ", type='" + getTypeFMT() + "'"
            + ", args=" + getArgsFMT()
            + "]"
            ;
    }
}
