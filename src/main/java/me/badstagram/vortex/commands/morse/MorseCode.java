package me.badstagram.vortex.commands.morse;

public enum MorseCode {
    A("a", ".-"),
    B("b", "-..."),
    C("c", "-.-."),
    D("d", "-.."),
    E("e", "."),
    F("f", "..-."),
    G("g", "--."),
    H("h", "...."),
    I("i", ".."),
    J("j", ".---"),
    K("k", "-.-"),
    L("l", ".-.."),
    M("m", "--"),
    N("n", "-."),
    O("o", "---"),
    P("p", ".--."),
    Q("q", "--.-"),
    R("r", ".-."),
    S("s", "..."),
    T("t", "-"),
    U("u", "..-"),
    V("v", "...-"),
    W("w", ".--"),
    X("x", "-..-"),
    Y("y", "-.--"),
    Z("z", "--.."),
    SLASH("/", "/");

    private final String letter, morse;

    MorseCode(String letter, String morse) {

        this.letter = letter;
        this.morse = morse;
    }

    public String getLetter() {
        return letter;
    }

    public String getMorse() {
        return morse;
    }

    public static MorseCode fromLetter(String letter) {
        for (var code : values()) {
            if (code.getLetter().equals(letter)) {
                return code;
            }
        }
        return null;
    }

    public static MorseCode fromMorse(String morse) {
        for (var code : values()) {
            if (code.getMorse().equals(morse)) {
                return code;
            }
        }
        return null;
    }
}
