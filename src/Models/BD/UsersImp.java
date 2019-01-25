package Models.BD;

import Controllers.Libraries.ImgLib;
import Models.BD.DAO.UserDAO;
import Models.User;
import java.awt.Image;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MrRainx
 */
public class UsersImp extends User implements UserDAO {

    public UsersImp(int IdUser, String UserName, String Password, String Name, Image Photo) {
        super(IdUser, UserName, Password, Name, Photo);
    }

    public UsersImp() {
    }

    @Override
    public boolean Insert() {

        String INSERT = "INSERT INTO "
                + " usuarios(username,password,nombre,foto)"
                + " VALUES( "
                + " '" + getUserName() + "',"
                + " set_byte( MD5('" + getPassword() + "')::bytea, 4,64),"
                + " '" + getName() + "',"
                + " '" + ImgLib.setImageInBase64(getPhoto()) + "'"
                + ")";
        
        return ResourceManager.Statement(INSERT) == null;

    }

    @Override
    public List<User> SelectAll() {

        String SELECT = "SELECT * FROM usuarios";

        List<User> List = new ArrayList<>();

        try {
            ResultSet rs = ResourceManager.Query(SELECT);
            
            while (rs.next()) {
                List.add(getUserFromRs(rs));
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return List;

    }

    @Override
    public List<User> SelectOne() {
        String SELECT = "SELECT  * "
                + " FROM usuarios "
                + " WHERE "
                + " username = '" + getUserName() + "'"
                + " AND "
                + " password = set_byte( MD5('" + getPassword() + "')::bytea, 4,64)";

        List<User> List = new ArrayList<>();

        try {
            ResultSet rs = ResourceManager.Query(SELECT);

            while (rs.next()) {
                List.add(getUserFromRs(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return List;
    }

    @Override
    public boolean Update(int Pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean Delete(int Pk) {
        
        String DELETE = "DELETE FROM"
                + " usuarios"
                + " WHERE idusuario = "+Pk;
        
        return ResourceManager.Statement(DELETE) == null;
        
    }

    private User getUserFromRs(ResultSet rs) {
        User user = new User();
        byte[] bytePhoto;
        try {

            user.setIdUser(rs.getInt("idusuario"));
            user.setUserName(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("nombre"));
            bytePhoto = rs.getBytes("foto");

            if (bytePhoto != null) {

                user.setPhoto(ImgLib.ByteToImage(bytePhoto));

            } else {
                user.setPhoto(null);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsersImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    
    /*
    public static void main(String[] args) {
        
        UsersImp user = new UsersImp();
        
    }
    */
}
