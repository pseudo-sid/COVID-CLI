import java.util.*;

class Patient{
    private final String name;
    String getName(){
        return this.name;
    }
    private final int age;
    int getAge(){
        return this.age;
    }
    private final int oxylev;
    int getOxylev(){
        return this.oxylev;
    }
    private final double temp;
    double getTemp(){
        return this.temp;
    }
    private int UID;
    int getUID(){
        return this.UID;
    }
    void setUID(int i){
        this.UID = i;
    }
    private int rec_days;
    int getRec_days(){
        return this.rec_days;
    }
    private String hosp_name;
    String getHosp_name(){
        return this.hosp_name;
    }

    Patient(String n, int a, int o, double t, int u){
        name = n;
        age = a;
        oxylev = o;
        temp = t;
        UID = u;
        hosp_name = "";
        rec_days = 0;
    }
    Patient(Patient p){
        this.name = p.name;
        this.age = p.age;
        this.oxylev = p.oxylev;
        this.temp = p.temp;
        this.UID = p.UID;
        this.rec_days = p.rec_days;
        this.hosp_name = p.hosp_name;
    }
    void admit(String n, int r){
        hosp_name = n;
        rec_days = r;
    }

    @Override
    public String toString() {
        String s = name + "\nTemperature is " + temp + "\nOxygen level is " + oxylev;
        if(hosp_name == "")
            s += "\nAdmission Status-Not Admitted";
        else
        {
            s += "\nAdmission Status-Admitted";
            s += "\nAdmitting Institute-" + hosp_name;
        }
        return s;
    }
}

class Hospital{
    private String status;
    String getStatus(){
        return this.status;
    }

    private final String name;
    String getName(){
        return this.name;
    }

    private ArrayList<Patient> pts = new ArrayList<>();
    ArrayList<Patient> getPts(){
        return this.pts;
    }
    private int numb_beds;
    int getNumb_beds(){
        return this.numb_beds;
    }
    private final double tem_cr;
    double getTem_cr(){
        return this.tem_cr;
    }
    private final int ox;
    int getOx(){
        return this.ox;
    }


    Hospital(String n, double t, int o, int b){
        name = n;
        status = "OPEN";
        numb_beds = b;
        tem_cr = t;
        ox = o;
    }
    public boolean eligible(Patient p){
        return ox <= p.getOxylev();
    }

    public void admit(Patient p){
        numb_beds--;
        pts.add(p);
        if(numb_beds == 0)
            status = "CLOSED";
    }

    @Override
    public String toString() {
        String s = name + "\nTemperature should be <= " + tem_cr +
                "\nOxygen levels should be >= " + ox +
                "\nNumber of Available beds-" + numb_beds +
                "\nAdmission Status-" + status;

        return s;
    }
}


class PatientCmp implements Comparator<Patient> {
    @Override
    public int compare(Patient p1, Patient p2) {
        if (p1.getOxylev() > p2.getOxylev())
            return 1;
        else if (p1.getOxylev() < p2.getOxylev())
            return -1;
        return Double.compare(p2.getTemp(), p1.getTemp());
    }
}
public class main {
    /*
    public static int check_admit(ArrayList<Patient> p){
        int ct = 0;
        for(Patient pt: p){
            if(pt.hosp_name == "")
                ct++;
        }
        return ct;
    }
     */

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int NP = sc.nextInt();

        Hashtable<Integer, Patient> pts = new Hashtable<>();
        Hashtable<String, Hospital> hs = new Hashtable<>();
        Hashtable<Integer, Patient> wls = new Hashtable<>();
        ArrayList<Patient> jo = new ArrayList<>();
        for(int i = 1; i <= NP; i++){
            String n = sc.next();
            double t = sc.nextDouble();
            int o = sc.nextInt();
            int a = sc.nextInt();
            Patient p = new Patient(n,a,o,t,i);
            jo.add(p);
            /*
            Patient p2 = new Patient(p);
            pts.put(i, p);
            wls.put(i, p2);
             */
        }
        Collections.sort(jo, new PatientCmp());
        int i = 0;
        for(Patient p: jo){
            p.setUID(++i);
            pts.put(i, p);
            wls.put(i, new Patient(p));
        }

        int q;
        while(!wls.isEmpty()){
            q = sc.nextInt();
            switch (q){
                case 1: {

                    String hsp = sc.next();
                    System.out.println("");
                    System.out.print("Temperature Criteria-");
                    double t = sc.nextDouble();
                    System.out.print("Oxygen Levels–");
                    int ox = sc.nextInt();
                    System.out.print("Number of Available beds-");
                    int b = sc.nextInt();
                    Hospital h = new Hospital(hsp, t, ox, b);
                    System.out.println("\n"+h);

                    Set<Integer> keys = wls.keySet();
                    List<Integer> list = new ArrayList<>(keys);
                    Collections.sort(list);

                    ArrayList<Integer> removed = new ArrayList<>();
                    for (int x : list) {
                        if (h.getStatus() == "CLOSED")
                            break;
                        else {
                            Patient d = wls.get(x);
                            Patient e = pts.get(x);
                            if (h.eligible(d)) {
                                System.out.print("Recovery days for admitted patient ID " + d.getUID() + "-");
                                int c = sc.nextInt();
                                e.admit(hsp, c);
                                h.admit(new Patient(e));
                                removed.add(x);
                            }

                        }
                    }
                    for(int x: removed){
                        wls.remove(x);
                    }

                    keys = wls.keySet();
                    list = new ArrayList<>(keys);
                    Collections.sort(list);
                    removed = new ArrayList<>();

                    if (h.getStatus() == "OPEN") {
                        for (int x : keys) {
                            if (h.getStatus() == "CLOSED")
                                break;
                            else {
                                Patient d = wls.get(x);
                                Patient e = pts.get(x);
                                if (d.getTemp() <= h.getTem_cr()) {
                                    System.out.print("Recovery days for admitted patient ID " + d.getUID() + "-");
                                    int c = sc.nextInt();
                                    e.admit(hsp, c);
                                    h.admit(new Patient(e));
                                    removed.add(x);
                                }
                            }
                        }
                    }
                    for(int x: removed){
                        wls.remove(x);
                    }
                    hs.put(hsp, h);
                }
                    break;

                case 2:
                {

                    System.out.println("\nAccount ID removed of admitted patients");
                    Set<Integer> ks = pts.keySet();
                    List<Integer> list = new ArrayList<>(ks);
                    Collections.sort(list);
                    ArrayList<Integer> removed = new ArrayList<>();
                    for(int x : list){
                        if(pts.get(x).getHosp_name() != ""){
                            System.out.println(x);
                            removed.add(x);
                        }
                    }
                    for(int x: removed){
                        pts.remove(x);
                    }
                }
                    break;
                case 3:
                {
                    System.out.println("\nAccounts removed of institutes whose admission is closed");
                    Set<String> ks = hs.keySet();
                    ArrayList<String> removed = new ArrayList<>();
                    for(String x: ks){
                        if(hs.get(x).getStatus().equals("CLOSED"))
                        {
                           Hospital tmp =  hs.get(x);
                           System.out.println(tmp.getName());
                           removed.add(x);
                        }
                    }
                    for(String s: removed){
                        hs.remove(s);
                    }

                }
                    break;
                case 4:
                    System.out.println("\n" + wls.size() + " patients");
                    break;
                case 5:
                    int ct = 0;
                    for(Hospital h : hs.values()){
                        if(h.getStatus().equals("OPEN"))
                            ct++;
                    }
                    System.out.println("\n" + ct + " institutes are admitting patients currently");
                    break;
                case 6:
                    System.out.println("\n" + hs.get(sc.next()));
                    break;
                case 7:
                {
                    int id = sc.nextInt();
                    System.out.println("\n" + pts.get(id));
                }
                    break;
                case 8:
                {
                    Set<Integer> keys = pts.keySet();
                    List<Integer> list = new ArrayList<>(keys);
                    Collections.sort(list);
                    System.out.println("");
                    for(int x: list) {
                        Patient p = pts.get(x);
                        System.out.println("" + p.getUID() + " " + p.getName());
                    }
                }
                    break;
                case 9: {
                    String s = sc.next();
                    Hospital h = hs.get(s);
                    System.out.println("");
                    for (Patient p : h.getPts()) {
                        System.out.println(p.getName() +  ", recovery time is " + p.getRec_days() + " days");
                    }
                }
                    break;
                default:
                    System.out.println("Wrong Input!");
            }
        }
        System.out.println("\n All patients were successfully admitted!");
    }
}
