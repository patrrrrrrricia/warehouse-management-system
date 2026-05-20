package dao;

import model.Bill;

/**
 * BILL- clasa pentru accesul la datele entitatii bill
 *     - mosteneste functionalitatile crud generice din AbstractDao
 * si este folosita pentru a gestiona operatiunile cu facturi in baza de date
 */
public class BillDAO extends AbstractDAO<Bill>
{
    // genereaza automat query urile sql pe baza numelui clasei bill
    // si a campurilor din record
}