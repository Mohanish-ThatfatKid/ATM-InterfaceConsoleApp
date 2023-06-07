package in.proj.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.proj.model.Account;
import in.proj.model.AccountHolder;
import in.proj.model.Transaction;
import in.proj.utils.JdbcUtil;

public class UserServiceImpl implements IuserService {

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean flag = false;

	@Override
	public boolean saveTransaction(String action, String accnumber, Double money) {

		boolean flag = false;
		int row = 0;
		try {
			conn = JdbcUtil.getConnection();
			if (conn != null) {
				String sqlTransactionQuery = "insert into transactions (`action`,`accountNumber`,`amount`) values(?,?,?)";
				pstmt = conn.prepareStatement(sqlTransactionQuery);
				pstmt.setString(1, action);
				pstmt.setString(2, accnumber);
				pstmt.setDouble(3, money);
				row = pstmt.executeUpdate();
				if (row > 0) {
					flag = true;
				} else {
					flag = false;
				}

			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}finally {
			try {
				JdbcUtil.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return flag;
	}

	@Override
	public Double checkBalance(String accNumber) {
		Double balance = 0.0;

		try {
			conn = JdbcUtil.getConnection();
			if (conn != null) {
				String selectQuery = "select balance from account where accountNumber = ?";
				pstmt = conn.prepareStatement(selectQuery);
				pstmt.setString(1, accNumber);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					balance = rs.getDouble(1);
				}
			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				JdbcUtil.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return balance;
	}

	@Override
	public Double deposite(AccountHolder accHolder, Double money) {
		int rowaffected = 0;
		boolean result = false;
		Double balance = 0.0;
		try {
			conn = JdbcUtil.getConnection();
			if (conn != null) {
				String sqlDepositeQuery = "update account set balance= balance+? where accountNumber=?";
				pstmt = conn.prepareStatement(sqlDepositeQuery);
				pstmt.setDouble(1, money);
				pstmt.setString(2, accHolder.getAccount().getAccNumber());

				rowaffected = pstmt.executeUpdate();
				if (rowaffected > 0) {
					result = saveTransaction("Deposite", accHolder.getAccount().getAccNumber(), money);

					if (result) {
						balance = checkBalance(accHolder.getAccount().getAccNumber());
					}

				}

			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				JdbcUtil.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return balance;
	}

	@Override
	public String withdraw(AccountHolder accHolder, Double money) {
		String accnumber = accHolder.getAccount().getAccNumber();
		Double balance = checkBalance(accnumber);
		Double newBalance = 0.0;
		int rowAffected = 0;
		boolean result = false;
		try {
			conn = JdbcUtil.getConnection();
			if (conn != null) {
				newBalance = balance - money;
				if (balance <= 3000 || newBalance <= 3000) {
					return "not Enough";
				} else {
					String sqlUpdateQuery = "update account set balance=? where accountNumber=?";
					pstmt = conn.prepareStatement(sqlUpdateQuery);
					pstmt.setDouble(1, newBalance);
					pstmt.setString(2, accnumber);

					rowAffected = pstmt.executeUpdate();
					if (rowAffected > 0) {
						result = saveTransaction("Withdraw", accnumber, money);
					}
					return "success";
				}

			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Fail";
		}finally {
			try {
				JdbcUtil.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (result) {
			return "Success";
		} else {
			return "Fail";
		}
	}

	@Override
	public boolean changePin(String userid, int newPin) {

		boolean flag = false;
		int rowaffected = 0;
		try {
			conn = JdbcUtil.getConnection();
			if (conn!= null) {
				String squlUpdateQuery = "update accholder set userPin = ? where userId=? ";
				pstmt = conn.prepareStatement(squlUpdateQuery);
				pstmt.setInt(1, newPin);
				pstmt.setString(2, userid);
				
			 rowaffected =	pstmt.executeUpdate();
			 if (rowaffected>0) {
				 flag = true;
			}
			 else {
				flag = false;
			}
				
			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}finally {
			try {
				JdbcUtil.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return flag;
	}

	@Override
	public List<Transaction> getTransactions(AccountHolder accountHolder) {
		String accNumber = accountHolder.getAccount().getAccNumber();

		List<Transaction> trans = new ArrayList<Transaction>();
		try {
			conn = JdbcUtil.getConnection();
			if (conn != null) {
				String sqlSelectQuery = "select action,amount from transactions where accountNumber=?";
				pstmt = conn.prepareStatement(sqlSelectQuery);
				pstmt.setString(1, accNumber);
				
				rs = pstmt.executeQuery();
				if (rs.next()) {
					while(rs.next()) {
						Transaction transaction = new Transaction();
						transaction.setTransactiontype(rs.getString(1));
						transaction.setMoney(rs.getDouble(2));
						
						trans.add(transaction);
					}					
				}else {
					trans = null;
				}

			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				JdbcUtil.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return trans;
	}

	@Override
	public AccountHolder login(String userid, int pin) {
		String userName = "";
		String accNumber = "";

		Account acc = new Account();
		AccountHolder acchold = null;
		try {
			conn = JdbcUtil.getConnection();
			if (conn != null) {
				String checkQuery = "select userName,accountNumber from accholder where userId=? and userPin =?";
				pstmt = conn.prepareStatement(checkQuery);
				pstmt.setString(1, userid);
				pstmt.setInt(2, pin);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					acchold = new AccountHolder();

					userName = rs.getString(1);
					accNumber = rs.getString(2);

					acc.setAccNumber(accNumber);
					acchold.setAccholName(userName);
					acchold.setAccount(acc);
				}

			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				JdbcUtil.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return acchold;
	}

	@Override
	public Double transferMoney(AccountHolder accHolder, String recieverAccNumber, Double money) {

		String accNumber = accHolder.getAccount().getAccNumber();
		Double balance = checkBalance(accNumber);
		int rowAffected = 0;
		boolean result = false;
		
		Double newBalance = balance-money;
		if (balance<=3000 || newBalance<3000 ) {
			return balance;
		}
		else {
			try {
				conn = JdbcUtil.getConnection();
				
				String sqlUpdateQuery = "update account set balance=? where accountNumber=?";
				pstmt = conn.prepareStatement(sqlUpdateQuery);
				pstmt.setDouble(1, newBalance);
				pstmt.setString(2, accNumber);
				
				rowAffected = pstmt.executeUpdate();
				if (rowAffected > 0) {
					result = saveTransaction("Transfer", accNumber, money);
				}
			} catch (IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally {
				try {
					JdbcUtil.closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return newBalance;
	}

}
