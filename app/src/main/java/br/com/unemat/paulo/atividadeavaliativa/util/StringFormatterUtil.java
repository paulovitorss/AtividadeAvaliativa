package br.com.unemat.paulo.atividadeavaliativa.util;

public class StringFormatterUtil {

    /**
     * Formata uma string de CPF (11 dígitos) para o padrão XXX.XXX.XXX-XX.
     *
     * @param cpf O CPF contendo apenas os números.
     * @return O CPF formatado, ou a string original se a entrada for inválida.
     */
    public static String formatCPF(String cpf) {
        if (cpf == null) return "";
        String cleanCpf = cpf.replaceAll("\\D", "");

        if (cleanCpf.length() != 11) {
            return cpf;
        }

        return cleanCpf.substring(0, 3) + "." +
                cleanCpf.substring(3, 6) + "." +
                cleanCpf.substring(6, 9) + "-" +
                cleanCpf.substring(9, 11);
    }

    /**
     * Formata uma string de número de telefone (10 ou 11 dígitos) para os padrões
     * (XX) XXXX-XXXX ou (XX) X XXXX-XXXX.
     *
     * @param phone O número de telefone contendo apenas os números.
     * @return O telefone formatado, ou a string original se a entrada for inválida.
     */
    public static String formatPhoneNumber(String phone) {
        if (phone == null) return "";
        String cleanPhone = phone.replaceAll("\\D", "");

        if (cleanPhone.length() == 11) {
            return "(" + cleanPhone.substring(0, 2) + ") " +
                    cleanPhone.charAt(2) + " " +
                    cleanPhone.substring(3, 7) + "-" +
                    cleanPhone.substring(7, 11);
        } else if (cleanPhone.length() == 10) {
            return "(" + cleanPhone.substring(0, 2) + ") " +
                    cleanPhone.substring(2, 6) + "-" +
                    cleanPhone.substring(6, 10);
        }

        return phone;
    }
}