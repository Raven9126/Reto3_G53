package Vistas;

import Modelo.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Controlador.*;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
/*
 * @author Enith Vargas PB G53
 */
public class UserMenu extends javax.swing.JFrame {
    //1. Instancia de la clase Conexión;
    Conexion conexion = new Conexion ();
    Connection connection;
    //2. La librería Statement permite ejecutar los query SQL
    Statement st;
    ResultSet rs;
    //3. Creamos una instancia de la tabla de la interfaz
    DefaultTableModel contenidoTablaEmpleados;
    DefaultTableModel contenidoTablaDepartamentos;
    ComboBoxModel modelEnumDepartamentos;
    ComboBoxModel modelEnumZona;
    ComboBoxModel modelEnumTipoCalle;
    
    public UserMenu() {
        modelEnumDepartamentos = new DefaultComboBoxModel(EnumDepartamento.values());
	modelEnumZona = new DefaultComboBoxModel(EnumZona.values());
	modelEnumTipoCalle = new DefaultComboBoxModel(EnumTipoCalle.values());
        initComponents();
        setLocationRelativeTo(null);
        listarEmpleados();
        ListarDepartamentos();
    }
   
    //4. Método que trae todos los empleados existentes en la base de datos
    private void listarEmpleados(){
        String nombre = txtBuscarE.getText();
        if(nombre.isEmpty()){
            String queryConsulta = "SELECT * FROM empleado";
            //5. Intentar ejecutar el query y obtener una respuesta de la base de datos
            try{
                connection = conexion.getConnection();
                st = connection.createStatement();
                rs = st.executeQuery(queryConsulta);
                //6. Crear un objeto donde se almacene el resultado de la consulta
                Object[] empleados = new Object [6];
                //7. Actualizar la propiedad Model de la tabla
                contenidoTablaEmpleados = (DefaultTableModel)tblEmpleados.getModel();
                //8. Recorremos el resultado de la consulta del query
                while(rs.next()){
                    empleados[0] = rs.getInt("IdEmp");
                    empleados[1] = rs.getString("nombreEmp");
                    empleados[2] = rs.getString("apellidos");
                    empleados[3] = rs.getString("tipoDocumento");
                    empleados[4] = rs.getString("documento");
                    empleados[5] = rs.getString("correo");
                    //9. Creamos una fila dentro de la tabla por cada empleado que devuelve la consulta
                    contenidoTablaEmpleados.addRow(empleados);
                    System.out.println("id: " + empleados[0] + ", nombre: " + empleados[1] + " , apellido: " + empleados[2] + ", documento: "
			    + empleados[3] + " " + empleados[4] + ", correo: " + empleados[5]);
                    
                }
                
                tblEmpleados.setModel(contenidoTablaEmpleados);
            }catch(SQLException e){
                System.out.println("Error");
            }
        }else{
            String query = "SELECT * FROM `empleado` WHERE nombreEmp LIKE '%" + nombre + "%' OR apellidos LIKE '%" + nombre + "%';";
            try{
                connection = conexion.getConnection();
                //Creamos el queryConsulta
                st = connection.createStatement();
                //Ejecutamos el query que tiene la variable queryConsulta
                rs = st.executeQuery(query);
                //Creamos un objeto que almacenará todos los registros de empleados
                //que existen en la base de datos
                Object[] empleados = new Object [6];
                contenidoTablaEmpleados = (DefaultTableModel)tblEmpleados.getModel();
                //Mientras el resultado del queryCounsulta encuentre registros en la
                //base de datos se ingresa al while
                while(rs.next()){
                    empleados[0] = rs.getInt("IdEmp");
                    empleados[1] = rs.getString("nombreEmp");
                    empleados[2] = rs.getString("apellidos");
                    empleados[3] = rs.getString("tipoDocumento");
                    empleados[4] = rs.getString("documento");
                    empleados[5] = rs.getString("correo");
                    contenidoTablaEmpleados.addRow(empleados);
                    System.out.println(rs.getString("IdEmp") + " " + rs.getString("nombreEmp")+ " " + rs.getString("apellidos"));
                }
                tblEmpleados.setModel(contenidoTablaEmpleados);
            }catch(SQLException e){
                System.out.println("Error");
        }
        }      
    }
    
    // Cuando añado un nuevo empleado el proceso background es el siguiente:
    //1. Se almacena el nuevo empleado en la base de datos
    //2. Se eliminan los datos que tiene la tabla tblEmpleados
    //3. Se llama el método listarEmpleados para consultar nuevamente la bd
    //4. Se cargan los empleados en la tabla incluyendo el recién creado
    public void BorrarDatosTabla() {
	// for loop to deleted each element  on each row inside the table 
	for (int i = 0; i < tblEmpleados.getRowCount(); i++) {
	    //removing each row inside the table with removeRow
	    contenidoTablaEmpleados.removeRow(i);
	    i -= 1;
	}
    }
    
    private void ListarDepartamentos(){
        String queryDepartamentos = "SELECT DISTINCT nombreDepartamento, zona from `direccion`;";
	try {
		connection = conexion.getConnection();
		//Creating  queryConsulta
		st = connection.createStatement();
		// Exe the query from the variable queryConsulta
		rs = st.executeQuery(queryDepartamentos);
		// Object to get all elements from the data base to a jtable

		Object[] departamentos = new Object[2];
		contenidoTablaDepartamentos = (DefaultTableModel) tblDepartamentos.getModel();
		// While loop to run every element from the query

		while (rs.next()) {
		    departamentos[0] = rs.getString("nombreDepartamento");
		    departamentos[1] = rs.getString("zona");
		  
		    // making a new row on the jtable for each employee
		    contenidoTablaDepartamentos.addRow(departamentos);
		    //Sout to print each employee data 
		    System.out.println("Departamento: " +departamentos[0] + " , zonas:" + departamentos[1]);   
		    tblDepartamentos.setModel(contenidoTablaDepartamentos);
		}

	    } catch (SQLException e) {
		   
		System.out.println("Error de creación de tabla de departamentos");
		System.out.println(e);
	    }
    }

    public void borrarDatosTablaDepartamentos(){
        for (int i = 0; i < tblDepartamentos.getRowCount(); i++) {
	    //removing each row inside the table with removeRow
	    contenidoTablaDepartamentos.removeRow(i);
	    i -= 1;
	}
    }
    public void eliminarEmpleado(){
        //1. Identificamos la fila seleccionada en la tabla de Empleados
        int row = tblEmpleados.getSelectedRow();
        //2. Capturamos el id del empleado que corresponda a la fila seleccionada
        int id = Integer.parseInt(tblEmpleados.getValueAt(row, 0).toString());
        if (row < 0){
            JOptionPane.showMessageDialog(this, "Debes seleccionar un empleado.", "", JOptionPane.WARNING_MESSAGE);
        }else{
            String query = "DELETE FROM empleado WHERE idEmp = " +id+ ";";
            System.out.println(query);
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabHome = new javax.swing.JTabbedPane();
        tabEmployees = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnAddUser = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        btnSearch = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarE = new javax.swing.JTextField();
        tabSucursales = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSucursal = new javax.swing.JTextField();
        cbDepartamento = new javax.swing.JComboBox<>();
        cbZona = new javax.swing.JComboBox<>();
        cbTipoDireccion = new javax.swing.JComboBox<>();
        txtNumero1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNumero2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNumero3 = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnListar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDepartamentos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setLocationByPlatform(true);

        jPanel4.setBackground(new java.awt.Color(102, 153, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/newUser.png"))); // NOI18N
        btnAddUser.setText("Add Employee");
        btnAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUserActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("EMPLOYEES DATA");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logo.png"))); // NOI18N

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Number", "Name", "Last Name", "Document type", "Document", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpleadosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmpleados);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/showUser.png"))); // NOI18N
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Buscar");

        txtBuscarE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarEKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(60, 60, 60)
                            .addComponent(jLabel1))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtBuscarE, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(112, 112, 112)
                            .addComponent(btnAddUser))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 39, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnSearch)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBuscarE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout tabEmployeesLayout = new javax.swing.GroupLayout(tabEmployees);
        tabEmployees.setLayout(tabEmployeesLayout);
        tabEmployeesLayout.setHorizontalGroup(
            tabEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEmployeesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabEmployeesLayout.setVerticalGroup(
            tabEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEmployeesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabHome.addTab("Employees", tabEmployees);

        tabSucursales.setBackground(new java.awt.Color(102, 153, 255));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Sucursal");

        jLabel5.setText("Departamento");

        jLabel6.setText("Zona");

        jLabel7.setText("Tipo Dirección");

        cbDepartamento.setModel(modelEnumDepartamentos);

        cbZona.setModel(modelEnumZona);

        cbTipoDireccion.setModel(modelEnumTipoCalle);

        jLabel8.setText("#");

        jLabel9.setText("-");

        btnSave.setText("Save");
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });

        btnListar.setText("Ver Sucursales");

        tblDepartamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Departamento", "Zona"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblDepartamentos);

        javax.swing.GroupLayout tabSucursalesLayout = new javax.swing.GroupLayout(tabSucursales);
        tabSucursales.setLayout(tabSucursalesLayout);
        tabSucursalesLayout.setHorizontalGroup(
            tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSucursalesLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabSucursalesLayout.createSequentialGroup()
                        .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSucursalesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSave)
                                .addGap(18, 18, 18)
                                .addComponent(btnListar))
                            .addGroup(tabSucursalesLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cbTipoDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cbDepartamento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtSucursal, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cbZona, 0, 144, Short.MAX_VALUE)))))
                        .addGap(3, 3, 3)
                        .addComponent(txtNumero1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addGap(4, 4, 4)
                        .addComponent(txtNumero2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumero3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabSucursalesLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(450, Short.MAX_VALUE))
        );
        tabSucursalesLayout.setVerticalGroup(
            tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSucursalesLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbZona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbTipoDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumero1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtNumero2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtNumero3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tabSucursalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnListar))
                .addGap(33, 33, 33)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        tabHome.addTab("Sucursales", tabSucursales);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabHome, javax.swing.GroupLayout.PREFERRED_SIZE, 624, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabHome, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        BorrarDatosTabla();
        listarEmpleados();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        //Creamos una instancia del diálogo
        AddUserForm addUserF = new AddUserForm(this, rootPaneCheckingEnabled);
        addUserF.setVisible(true);
        BorrarDatosTabla();
        listarEmpleados();
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void tblEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadosMouseClicked
        // get information from row num
        int row = tblEmpleados.getSelectedRow();
        System.out.println("Fila seleccionada: " + row);

        //Validate data
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un  empleado ", " ", JOptionPane.WARNING_MESSAGE);

        } else {
            //Get employee (id, name, surname,  id type , id num , e-mail )
            int id = Integer.parseInt(tblEmpleados.getValueAt(row, 0).toString());
            // row 0 column 1 =  employee name
            String nombre = (String) tblEmpleados.getValueAt(row, 1);
            String apellidos = (String) tblEmpleados.getValueAt(row, 2);
            String tipoDocumento = (String) tblEmpleados.getValueAt(row, 3);
            String documento = (String) tblEmpleados.getValueAt(row, 4);
            String correo = (String) tblEmpleados.getValueAt(row, 5);
            System.out.println("id: " + id + ", empleado: " + nombre + " " + apellidos
                + ", tipo documento: " + tipoDocumento + ", numero: " + documento + ",  correo: " + correo);

            // instance for jDialog to show the selected employee info
            ShowUserForm showUserForm = new ShowUserForm(this, true);
            showUserForm.recibirDatos(id, nombre, apellidos, documento, correo);
            showUserForm.setVisible(true);

            //update data from table
            BorrarDatosTabla();
            listarEmpleados();

        }
    }//GEN-LAST:event_tblEmpleadosMouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        BorrarDatosTabla();
        listarEmpleados();
    }//GEN-LAST:event_btnSearchMouseClicked

    private void txtBuscarEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BorrarDatosTabla();
            listarEmpleados();
        }
    }//GEN-LAST:event_txtBuscarEKeyPressed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        int departamentoOption = cbDepartamento.getSelectedIndex();
        int zonaOption = cbZona.getSelectedIndex();
        int tipoCalleOption = cbTipoDireccion.getSelectedIndex();
        String numero1 = txtNumero1.getText();
        String numero2 = txtNumero2.getText();
        String numero3 = txtNumero3.getText();
        String zonaOptionT = "";
        String tipoCalleOptionT = "";
        String departamentoOptionT = "";
        
        if (departamentoOption == 0) {
            departamentoOptionT = "Amazonas";      
        }else if (departamentoOption == 1) {
            departamentoOptionT = "Antioquia";      
        }else if (departamentoOption == 2) {
            departamentoOptionT = "Arauca";
        }else if (departamentoOption == 3) {
            departamentoOptionT = "Atlántico";
        }else if (departamentoOption == 4) {
            departamentoOptionT = "Bogotá";
        }else if (departamentoOption == 5) {
            departamentoOptionT = "Bolívar";
        }else if (departamentoOption == 6) {
            departamentoOptionT = "Boyacá";
        }else if (departamentoOption == 7) {
            departamentoOptionT = "Caldas";
        }else if (departamentoOption == 8) {
            departamentoOptionT = "Caquetá";
        }else if (departamentoOption == 9) {
            departamentoOptionT = "Casanare";
        }else if (departamentoOption == 10) {
            departamentoOptionT = "Cauca";
        }else if (departamentoOption == 11) {
            departamentoOptionT = "Cesar";
        }else if (departamentoOption == 12) {
            departamentoOptionT = "Chocó"; 
        }else if (departamentoOption == 13) {
            departamentoOptionT = "Córdoba";
        }else if (departamentoOption == 14) {
            departamentoOptionT = "Cundinamarca";
        }else if (departamentoOption == 15) {
            departamentoOptionT = "Guainía";
        }else if (departamentoOption == 16) {
            departamentoOptionT = "Guaviare";
        }else if (departamentoOption == 17) {
            departamentoOptionT = "Huila";
        }else if (departamentoOption == 18) {
            departamentoOptionT = "La Guajira";
        }else if (departamentoOption == 19) {
            departamentoOptionT = "Magdalena";
        }else if (departamentoOption == 20) {
            departamentoOptionT = "Meta";
        }else if (departamentoOption == 21) {
            departamentoOptionT = "Nariño";
        }else if (departamentoOption == 22) {
            departamentoOptionT = "Norte de Santander";
        }else if (departamentoOption == 23) {
            departamentoOptionT = "Putumayo";
        }else if (departamentoOption == 24) {
            departamentoOptionT = "Quindío";
        }else if (departamentoOption == 25) {
            departamentoOptionT = "Risaralda";
        }else if (departamentoOption == 26) {
            departamentoOptionT = "San Andrés y Providencia";
        }else if (departamentoOption == 27) {
            departamentoOptionT = "Santander";
        }else if (departamentoOption == 28) {
            departamentoOptionT = "Sucre";
        }else if (departamentoOption == 29) {
            departamentoOptionT = "Tolima";
        }else if (departamentoOption == 30) {
            departamentoOptionT = "Valle del Cauca";
        }else if (departamentoOption == 31) {
            departamentoOptionT = "Vaupés";
        }else {
            departamentoOptionT = "Vichada";
        }
        if (zonaOption == 0){
            zonaOptionT = "Urbana";
        }else {
            zonaOptionT = "Rural";
        }
        if(tipoCalleOption == 0){
	    tipoCalleOptionT = "Avenida";
	   
	} else if (tipoCalleOption == 1){
	    tipoCalleOptionT = "Calle";
	}else if (tipoCalleOption == 2){
	    tipoCalleOptionT = "Carrera";
	}else if (tipoCalleOption == 3){
	    tipoCalleOptionT = "Circunvalar";
	}else if (tipoCalleOption == 4){
	    tipoCalleOptionT = "Esquina";
	}else if (tipoCalleOption == 5){
	    tipoCalleOptionT = "Transversal";
	}else{
	    tipoCalleOptionT = "Otro";
	}
        String queryAddress = "INSERT INTO `direccion`(`zona`, `tipoCalle`, `numero1`, `numero2`, `numero3`, `nombreDepartamento`) VALUES ('"+zonaOptionT+"','"+ tipoCalleOptionT+"','"+numero1+"','"+numero2+"','"+numero3+"','"+departamentoOptionT+"')";
        System.out.println(queryAddress);
        try{
                connection = conexion.getConnection();
                st = connection.createStatement();
                st.executeUpdate(queryAddress);
                JOptionPane.showMessageDialog(this, "Dirección Creada.", "", JOptionPane.INFORMATION_MESSAGE);
            }catch (SQLException e){
                JOptionPane.showMessageDialog(this, "No fue posible crear Sucursal.", "", JOptionPane.ERROR_MESSAGE);
                System.out.println(e);
            } 
        borrarDatosTablaDepartamentos();
        ListarDepartamentos();
    }//GEN-LAST:event_btnSaveMouseClicked
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/* Set the Nimbus look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(UserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(UserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(UserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(UserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(() -> {
	    new UserMenu().setVisible(true);
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnListar;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cbDepartamento;
    private javax.swing.JComboBox<String> cbTipoDireccion;
    private javax.swing.JComboBox<String> cbZona;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel tabEmployees;
    private javax.swing.JTabbedPane tabHome;
    private javax.swing.JPanel tabSucursales;
    private javax.swing.JTable tblDepartamentos;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextField txtBuscarE;
    private javax.swing.JTextField txtNumero1;
    private javax.swing.JTextField txtNumero2;
    private javax.swing.JTextField txtNumero3;
    private javax.swing.JTextField txtSucursal;
    // End of variables declaration//GEN-END:variables

}
