import java.util.HashMap;

//Base class that expr and equ inherit from to gain letter def functionality
public abstract class MathStatement{
    protected  HashMap<String, Double> letterDefs = new HashMap<String, Double>(){{
       put("π", Math.PI); 
    }};
    protected void clearDefs(){letterDefs.clear();}
    
    
    
    
    //Helper
    public static double getNumVal(String num, HashMap<String, Double> letterValues){
        try{
            return Double.parseDouble(num);
        }
        catch(NumberFormatException e){
            if(letterValues.containsKey(num)) return letterValues.get(num);
            else{
                System.out.println("ERROR: letter "+num+" not defined");
                return 0;
            }
        }
    }
    
    
    
    
    //Letter defs mathods
    public void printDefs(){
        System.out.println(letterDefs);
    }
    public String[] defNames(){return letterDefs.keySet().toArray(new String[letterDefs.keySet().size()]);}
    public void addDef(String key, double val){
        letterDefs.put(key, val);
    }
    public boolean defsValid(){
        for(Double v : letterDefs.values()){
            if(v == null) return false;
        }
        return true;
    }
    public boolean isLetterDefined(String l){
        return letterDefs.get(l) != null;
    }
}