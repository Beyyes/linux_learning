package skyline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author CGF
 *
 */
public class Main {


    static List<Point> points = null;
    static int K = 3;
    static Map<Integer, ArrayList<Integer>> fatherRelation =
            new HashMap<Integer, ArrayList<Integer>>();
    static Map<Integer, ArrayList<Integer>> layerMap = new HashMap<Integer, ArrayList<Integer>>();
    static ArrayList<Integer> oneUnitGroup = new ArrayList<Integer>();
    
    public static void main(String[] args) throws IOException {
        points = loadFileData("data/test.txt");
        Collections.sort(points);

        // 二维的可以二分，多维的必须一个个判断
        buildDSG();
        System.out.println(fatherRelation.size());

//        for (Entry<Integer, ArrayList<Integer>> entry : fatherRelation.entrySet()) {
//            ArrayList<Integer> cList = entry.getValue();
//            System.out.println("son: " + points.get(entry.getKey()).number);
//            for (int i : cList) {
//                System.out.println(points.get(i).number);
//            }
//            System.out.println("----------");
//        }
        
        
        
    }

    public static void uWise() {
        
    }
    
    public static void buildDSG() {
        int maxLayer = 1;
        layerMap.put(1, new ArrayList<Integer>());
        layerMap.get(1).add(0);
        for (int pos = 1; pos < points.size(); pos++) {

            // 最后一层layer能支配当前点
            if (layerDominate(layerMap.get(maxLayer), points.get(pos))) {
                if (maxLayer + 1 > K) {
                    continue;
                }
                layerMap.put(++maxLayer, new ArrayList<Integer>());
                layerMap.get(maxLayer).add(pos);
                // 填加父亲关系
                for (int sLayer = 1; sLayer <= maxLayer; sLayer++) {
                    addFatherRelation(pos, layerMap.get(sLayer));
                }
                continue;
            }

            // 第一层layer不能支配当前点
            if (!layerDominate(layerMap.get(1), points.get(pos))) {
                layerMap.get(1).add(pos);
                continue;
            }

            // 判断从哪层开始不能支配该点
            for (int layer = 2; layer <= maxLayer; layer++) { // 这地方可把最后一层优化掉
                if (!layerDominate(layerMap.get(layer), points.get(pos))) {
                    layerMap.get(layer).add(pos);
                    for (int sLayer = 1; sLayer < layer; sLayer++) {
                        addFatherRelation(pos, layerMap.get(sLayer));
                    }
                    break;
                }
            }
        }
    }

    // 添加父子关系
    public static void addFatherRelation(int son, int father) {
        if (!fatherRelation.containsKey(son)) {
            fatherRelation.put(son, new ArrayList<Integer>());
        }
        fatherRelation.get(son).add(father);
    }

    // 添加父子关系
    public static void addFatherRelation(int son, ArrayList<Integer> fatherLists) {
        for (int father : fatherLists) {
            if (dominate(father, son)) {
                if (!fatherRelation.containsKey(son)) {
                    fatherRelation.put(son, new ArrayList<Integer>());
                }
                fatherRelation.get(son).add(father);
            }
        }
    }

    // 判断某一层是否有点能支配点p
    public static boolean layerDominate(ArrayList<Integer> arrays, Point p) {
        for (int i : arrays) {
            if (dominate(points.get(i), p)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Point> loadFileData(String filePath) throws IOException {
        ArrayList<Point> ans = new ArrayList<Point>();
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        int dimension = 0;
        int cnt = 0;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(" ");
            double[] x = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                x[i] = Double.parseDouble(values[i]);
            }
            Point p = new Point(++cnt, x);
            ans.add(p);
        }
        reader.close();
        return ans;
    }

    static class Point implements Comparable<Point> {
        public double[] x = new double[10];
        public int count = 0;
        public int number = 0;

        public Point(int num, double... values) {
            this.number = num;
            this.count = values.length;
            for (int i = 0; i < values.length; i++) {
                this.x[i] = values[i];
            }
        }

        public void output() {
            for (int i = 0; i < count; i++) {
                System.out.print(x[i] + " ");
            }
            System.out.println();
        }

        @Override
        public int compareTo(Point o) {
            if (x[0] > o.x[0])
                return 1;
            return -1;
        }
    }
    
    static class UnitGroup {
        public HashSet<Integer> fatherNodes = new HashSet<Integer>();
        public ArrayList 
    }
    
    public static boolean dominate(int pos1, int pos2) { // dominate的定义还需要注意
        Point p1 = points.get(pos1);
        Point p2 = points.get(pos2);
        int greatFlag = 0;
        int lessFlag = 0;
        for (int i = 0; i < p1.count; i++) {
            if (p1.x[i] > p2.x[i]) {
                greatFlag = 1;
            } else if (p1.x[i] < p2.x[i]) {
                lessFlag = 1;
            }
        }
        if (greatFlag == 0 && lessFlag == 1)
            return true;
        return false;
    }

    public static boolean dominate(Point p1, Point p2) {
        int greatFlag = 0;
        int lessFlag = 0;
        for (int i = 0; i < p1.count; i++) {
            if (p1.x[i] > p2.x[i]) {
                greatFlag = 1;
            } else if (p1.x[i] < p2.x[i]) {
                lessFlag = 1;
            }
        }
        if (greatFlag == 0 && lessFlag == 1)
            return true;
        return false;
    }
}
