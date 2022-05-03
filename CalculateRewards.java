import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CalculateRewards {
    public static void main(String []arguments) throws Exception {
        if(arguments.length > 0) {
            File file = new File(arguments[0]);
            BufferedReader bufferedReader;
            Map<String, Integer> customerWiseTotals = new HashMap<>();
            Map<String, Integer> customerMonthlyTotals = new HashMap<>();
            int numberOfRewards = 0;
            String[] recordTokens = null;
            String month = null;
            String keyForMonthlyTotals = null;
            try{
                bufferedReader = new BufferedReader(new FileReader(arguments[0]));
                String line = bufferedReader.readLine();
                while(line != null){
                    recordTokens = line.split(",");
                    numberOfRewards = getRewards(Integer.parseInt(recordTokens[2]));

                    //summing up the rewards per Customer and maintaining in a hashmap for efficient retrieval
                    if(customerWiseTotals.containsKey(recordTokens[0])){
                        customerWiseTotals.put(recordTokens[0],
                                customerWiseTotals.get(recordTokens[0])+numberOfRewards);
                    } else {
                        customerWiseTotals.put(recordTokens[0], numberOfRewards);
                    }

                    //get the month of the transaction
                    try {
                        month = getMonth(recordTokens[1]).toString();
                    }catch(Exception exception){
                        System.out.println("Invalid date format is found for "+recordTokens[0]+" as "+recordTokens[1]);
                    }
                    keyForMonthlyTotals = recordTokens[0]+"_"+month;

                    if(customerMonthlyTotals.containsKey(keyForMonthlyTotals)){
                        customerMonthlyTotals.put(keyForMonthlyTotals,
                                customerMonthlyTotals.get(keyForMonthlyTotals)+numberOfRewards);
                    } else {
                        customerMonthlyTotals.put(keyForMonthlyTotals,numberOfRewards);
                    }

                    line = bufferedReader.readLine();
                }
                summariseTheOutput(customerWiseTotals, customerMonthlyTotals);
            }catch(IOException ioException){
                throw new Exception("IOException occurred in opening the file");
            }
        } else {
            throw new Exception("No file path is received.");
        }
    }

    private static void summariseTheOutput(Map<String, Integer> customerWiseTotals, Map<String, Integer> customerMonthlyTotals) throws Exception {

        enterMethod("summariseTheOutput");
        if(customerWiseTotals.isEmpty()||customerMonthlyTotals.isEmpty()){
            throw new Exception("The data objects are Empty");
        } else {
            System.out.println("Customer wise Totals:");
            String key;
            Integer totalRewards = 0;
            for(Map.Entry mapElement : customerWiseTotals.entrySet()){
                key = (String)mapElement.getKey();
                totalRewards = (Integer)mapElement.getValue();
                System.out.println(key+":"+totalRewards);
            }
            System.out.println("Customer Month wise Totals:");
            for(Map.Entry mapElement : customerMonthlyTotals.entrySet()){
                key = (String)mapElement.getKey();
                totalRewards = (Integer)mapElement.getValue();
                System.out.println(key.split("_")[0]+":"+key.split("_")[1]+":"+totalRewards);
            }
        }
        exitMethod("summariseTheOutput");
    }

    private static void enterMethod(String methodName) {
        System.out.println("Entered method "+methodName);
    }

    private static void exitMethod(String methodName) {
        System.out.println("Exiting method "+methodName);
    }

    private static Month getMonth(String recordToken) throws Exception{

        enterMethod("getMonth");
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(recordToken);
        } catch(Exception exception){
            throw new Exception("Invalid date format found");
        }
        return localDate.getMonth();
    }

    private static int getRewards(int parseInt) {

        enterMethod("getRewards");
        int no_of_rewards = 0;
        int hundredAbove = parseInt > 100 ? (parseInt-100)*2 : 0;
        int fiftyAbove = hundredAbove>0 ? 50 : (parseInt>50 ? parseInt-50 : 0);
        no_of_rewards = hundredAbove + fiftyAbove;
        exitMethod("getRewards");
        return no_of_rewards;

    }
}
