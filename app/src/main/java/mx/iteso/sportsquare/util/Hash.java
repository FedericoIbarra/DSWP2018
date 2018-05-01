package mx.iteso.sportsquare.util;

/**
 * Created by federicoibarra on 09/03/18.
 */

public class Hash {

    /**
     * @return un String con el Hash del texto.
     * @param txt el texto a encriptar.
     * @param hashType el tipo de encriptacion.
     */

    public static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     *  @return un hash SHA1 a partir de un texto
     *  */
    public static String sha1(String txt) {
        return Hash.getHash(txt, "SHA1");
    }

}