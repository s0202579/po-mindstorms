package penoplatinum.navigator;

public class BarcodeDataNav {
    private final ColorInterpreter interpreter;
    public static byte[] expand = new byte[]{0,15,22,25,37,42,51,60,67,76,85,90,102,105,112,127};
    
    public BarcodeDataNav(ColorInterpreter interpreter)
    {
        this.interpreter = interpreter;
        
    }
    
    public int translate(BufferSubset list) {
        if(list.size()<7){return -1;}
        int val = 0;
        for (int i = 0; i < 7; i++) {
            int sum = 0;
            for (int j = (i * list.size()) / 7; j < (i + 1) * list.size() / 7; j++) {
                sum += list.get(j);
            }
            int averageValue = sum / (((i + 1) * list.size() / 7) - ((i * list.size()) / 7));
            val*=2;
            val+=interpreter.isBlackOrWhite(averageValue);
        }
        return val;
    }

        
      
    public int correct(int value) {
        byte corrected = (byte) (getBarcodesRepair(value) / 8);
        return corrected;
    }
    
    public static byte[] barcodesRepair = getBarcodes();
    public static byte[] getBarcodes(){
        byte[] out = new byte[128];
        for(int i=0;i<expand.length;i++){
            out[expand[i]]=expand[i];
            for(int j=1;j<128;j*=2){
                out[expand[i]^j]=expand[i];
            }
        }
        return out;
    }
    
    public static String[] codes = new String[]{"0000000","0001111","0010110","0011001","0100101","0101010","0110011","0111100","1000011","1001100","1010101","1011010","1100110","1101001","1110000","1111111"};
    
    public static byte getBarcodesRepair(int index){
        return barcodesRepair[index];
    }
    
    public static String getBarcodesString(int index){
        return codes[index];
    }
}
