package com.liaomj.util;

public class IPParser extends IPSeeker {
    // ��ַ ����ֻ����ecplise������ʹ�ã������ڷ������ϣ���Ҫ�Ƚ�qqwry.dat���ڼ�Ⱥ�ĸ����ڵ�ĳ���ж�ȡȨ��Ŀ¼��
    // Ȼ��������ָ��ȫ·��
    private static final String ipFilePath = "E:\\CentOS\\coding301-master (1)\\coding301\\hadoop-train-v2\\ip\\qqwry.dat";
    // �����ڷ�������
    //private static final String ipFilePath = "/opt/datas/qqwry.dat";
    private static IPParser obj = new IPParser(ipFilePath);



    protected IPParser(String ipFilePath) {
        super(ipFilePath);
    }

    public static IPParser getInstance() {
        return obj;
    }

    /**
     * ����ip��ַ
     *
     * @param ip
     * @return
     */
    public RegionInfo analyseIp(String ip) {
        if (ip == null || "".equals(ip.trim())) {
            return null;
        }

        RegionInfo info = new RegionInfo();
        try {
            String country = super.getCountry(ip);
            if ("������".equals(country) || country == null || country.isEmpty() || country.trim().startsWith("CZ88")) {
                // ����Ĭ��ֵ
                info.setCountry("�й�");
                info.setProvince("�Ϻ���");
            } else {
                int length = country.length();
                int index = country.indexOf('ʡ');
                if (index > 0) { // ��ʾ�ǹ��ڵ�ĳ��ʡ
                    info.setCountry("�й�");
                    info.setProvince(country.substring(0, Math.min(index + 1, length)));
                    int index2 = country.indexOf('��', index);
                    if (index2 > 0) {
                        // ������
                        info.setCity(country.substring(index + 1, Math.min(index2 + 1, length)));
                    }
                } else {
                    String flag = country.substring(0, 2);
                    switch (flag) {
                        case "����":
                            info.setCountry("�й�");
                            info.setProvince("���ɹ�������");
                            country = country.substring(3);
                            if (country != null && !country.isEmpty()) {
                                index = country.indexOf('��');
                                if (index > 0) {
                                    // ������
                                    info.setCity(country.substring(0, Math.min(index + 1, length)));
                                }
                                // TODO:��������������û�н��д���
                            }
                            break;
                        case "����":
                        case "����":
                        case "����":
                        case "�½�":
                            info.setCountry("�й�");
                            info.setProvince(flag);
                            country = country.substring(2);
                            if (country != null && !country.isEmpty()) {
                                index = country.indexOf('��');
                                if (index > 0) {
                                    // ������
                                    info.setCity(country.substring(0, Math.min(index + 1, length)));
                                }
                            }
                            break;
                        case "�Ϻ�":
                        case "����":
                        case "����":
                        case "���":
                            info.setCountry("�й�");
                            info.setProvince(flag + "��");
                            country = country.substring(3);
                            if (country != null && !country.isEmpty()) {
                                index = country.indexOf('��');
                                if (index > 0) {
                                    // ������
                                    char ch = country.charAt(index - 1);
                                    if (ch != 'С' || ch != 'У') {
                                        info.setCity(country.substring(0, Math.min(index + 1, length)));
                                    }
                                }

                                if ("unknown".equals(info.getCity())) {
                                    // ����city��û�����ã�������
                                    index = country.indexOf('��');
                                    if (index > 0) {
                                        // ������
                                        info.setCity(country.substring(0, Math.min(index + 1, length)));
                                    }
                                }
                            }
                            break;
                        case "���":
                        case "����":
                            info.setCountry("�й�");
                            info.setProvince(flag + "�ر�������");
                            break;
                        default:
                            info.setCountry(country); // ������������ip
                    }
                }
            }
        } catch (Exception e) {
            // nothing
        }
        return info;
    }

    /**
     * ip��ַ��Ӧ��info��
     *
     */
    public static class RegionInfo {
        private String country ;
        private String province ;
        private String city ;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "RegionInfo [country=" + country + ", province=" + province + ", city=" + city + "]";
        }
    }
}
