#include <iostream>
#include <math.h>
#include <fstream>
#include <string>
#include <array>
#include <sstream>

using namespace std;

const string input_file = "INPUT.txt";
//------------------------------------------------------------------------------------
// Functions
//------------------------------------------------------------------------------------
double string_to_double( const std::string& s) {
std::istringstream i(s);
double x;
if (!(i >> x)){
    return 0;
}
    return x;
}
int line_count(string file ){
ifstream in_data(file.c_str());
char cc;
int line_count = 0;
while (in_data.get(cc)){
    if (cc == '\n')
        ++line_count;
}
return line_count;
}
double read_data1(string file, int var){
ifstream in_data(file.c_str());
string line;
double data_set[line_count(file)];
int i = 0;
double data;
while (getline(in_data, line)){
    stringstream liner(line);
    string aa;
    liner >> aa;
    double a = string_to_double(aa);
    data_set[i]=a;
    i++;
}
return data = data_set[var];
}
double read_data2(string file, int var){
ifstream in_data(file.c_str());
string line;
double data_set[line_count(file)];
int i = 0;
double data;
while (getline(in_data, line)){
    stringstream liner(line);
    string aa,bb ;
    liner >> bb >> aa;
    double a = string_to_double(aa);
    data_set[i]=a;
    i++;
}
return data = data_set[var];
}
double read_data3(string file, int var){
ifstream in_data(file.c_str());
string line;
double data_set[line_count(file)];
int i = 0;
double data;
while (getline(in_data, line)){
    stringstream liner(line);
    string aa,bb ;
    liner >> bb >> bb >> aa;
    double a = string_to_double(aa);
    data_set[i]=a;
    i++;
}
return data = data_set[var];
}
//------------------------------------------------------------------------------
int main()
{
    int dec ;
    int i;
    double margin_prop = 2; // Propellant margin [percent]
    int n ;
    double m_prop_total = 0;
    double g_0 = 9.80665;

    double m_launch_max;
    double m_payload;

    cout << "Choose Manual |1| Auto |2| " << endl;
    cin >> dec ;
    if (dec==1)
        cout << "Manual" << endl;

    if (dec==2)
        cout << "Auto" << endl;



    cout << "Enter maximum launch mass: " << endl;
    cin >> m_launch_max ;
    cout << "Enter payload mass: " << endl;
    cin >> m_payload ;

    if (dec == 1) {
    cout << "Insert number of delta-V blocks: " << endl;
    cin >> n ;
    double ISP[n];
    double deltav[n];
    double m_prop[n];
    for (i=0;i<n;i++) {
        cout << "enter ISP for module " << i+1 << " : " << endl;
        cin >> ISP[i] ;
        cout << "enter delta-v for module " << i+1 << " : " << endl;
        cin >> deltav[i] ;
    }
   for (i=0;i<n;i++) {
    m_prop[i] = (m_launch_max-m_prop_total) * (1-1/exp(deltav[i]/(g_0 * ISP[i]))) ;
    m_prop_total = m_prop_total+m_prop[i];
    cout << "dV " << deltav[i] << " [m/s] ; m_prop " << m_prop[i] << " [kg] ; S/C " << m_launch_max - m_prop_total << " [kg] " << endl;
   }
   m_prop_total = m_prop_total + m_prop_total * margin_prop/100;
cout << "Remaining fuel at touchdown " << m_prop_total * margin_prop/100 << " [kg] " << endl;
cout << "Total fuel " << m_prop_total << " [kg] " << endl;
cout << "Maximum dry mass " << m_launch_max - m_prop_total - m_payload << " [kg] " << endl;
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    if (dec == 2 ) {
    n = line_count(input_file);
    double ISP[n];
    double deltav[n];
    double m_prop[n];
    double m_loss_add[n];

   for (i=0;i<n;i++) {
    ISP[i]    = read_data1(input_file, i);
    deltav[i] = read_data2(input_file, i);
    m_prop[i] = (m_launch_max-m_prop_total) * (1-1/exp(deltav[i]/(g_0 * ISP[i]))) + m_loss_add[i];
    m_prop_total = m_prop_total+m_prop[i];
    cout << "dV " << deltav[i] << " [m/s] ; m_prop " << m_prop[i] << " [kg] ; S/C " << m_launch_max - m_prop_total << " [kg] " << endl;
   }
   m_prop_total = m_prop_total + m_prop_total * margin_prop/100;
cout << "Remaining fuel at td " << m_prop_total * margin_prop/100 << " [kg] " << endl;
cout << "Total fuel " << m_prop_total << " [kg] " << endl;
cout << "Maximum dry mass " << m_launch_max - m_prop_total - m_payload << " [kg] " << endl;
    }
cin.get();
cin.get();
return 0;
}
