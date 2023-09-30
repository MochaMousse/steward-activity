package cc.mousse.steward.activity.util;

import cc.mousse.steward.activity.cache.BasicCache;
import com.google.gson.Gson;

import java.sql.*;
import java.util.*;

/**
 * @author PhineasZ
 */
public class DataSourceUtil {
  private static final Gson GSON = BasicCache.GSON;

  private DataSourceUtil() {}

  public static void init(String url, String username, String password) throws SQLException {
    BasicCache.DATA_SOURCE.setDriverClassName("com.mysql.cj.jdbc.Driver");
    BasicCache.DATA_SOURCE.setUrl(url);
    BasicCache.DATA_SOURCE.setUsername(username);
    BasicCache.DATA_SOURCE.setPassword(password);
    BasicCache.DATA_SOURCE.init();
  }

  public static <T> T one(Class<T> clazz, String sql, Object... args) {
    List<T> list = list(clazz, sql, args);
    return list.isEmpty() ? null : list.get(0);
  }

  public static void add(String sql, Object... args) {
    PreparedStatement preparedStatement = null;
    try (Connection connection = BasicCache.DATA_SOURCE.getConnection()) {
      preparedStatement = connection.prepareStatement(sql);
      int i = 1;
      for (Object arg : args) {
        preparedStatement.setObject(i++, arg);
      }
      preparedStatement.execute();
    } catch (Exception e) {
      LogUtil.warn(e);
    } finally {
      closeConnection(null, preparedStatement);
    }
  }

  public static void update(String sql, Object... args) {
    PreparedStatement preparedStatement = null;
    try (Connection connection = BasicCache.DATA_SOURCE.getConnection()) {
      preparedStatement = connection.prepareStatement(sql);
      int i = 1;
      for (Object arg : args) {
        preparedStatement.setObject(i++, arg);
      }
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      LogUtil.warn(e);
    } finally {
      closeConnection(null, preparedStatement);
    }
  }

  public static <T> List<T> list(Class<T> clazz, String sql, Object... args) {
    List<T> result = new ArrayList<>();
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try (Connection connection = BasicCache.DATA_SOURCE.getConnection()) {
      preparedStatement = connection.prepareStatement(sql);
      int i = 1;
      for (Object arg : args) {
        preparedStatement.setObject(i++, arg);
      }
      resultSet = preparedStatement.executeQuery();
      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      Map<String, Object> rowData = new HashMap<>(columnCount);
      while (resultSet.next()) {
        for (i = 1; i <= columnCount; i++) {
          rowData.put(metaData.getColumnLabel(i), resultSet.getObject(i));
        }
        result.add(GSON.fromJson(GSON.toJson(rowData), clazz));
      }
    } catch (Exception e) {
      LogUtil.warn(e);
    } finally {
      closeConnection(resultSet, preparedStatement);
    }
    return result;
  }

  public static List<String> list(String sql, Object... args) {
    List<String> result = new ArrayList<>();
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try (Connection connection = BasicCache.DATA_SOURCE.getConnection()) {
      preparedStatement = connection.prepareStatement(sql);
      int i = 1;
      for (Object arg : args) {
        preparedStatement.setObject(i++, arg);
      }
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        result.add((String) resultSet.getObject(1));
      }
    } catch (Exception e) {
      LogUtil.warn(e);
    } finally {
      closeConnection(resultSet, preparedStatement);
    }
    return result;
  }

  private static void closeConnection(ResultSet resultSet, PreparedStatement preparedStatement) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        LogUtil.warn(e);
      }
    }
    if (preparedStatement != null) {
      try {
        preparedStatement.close();
      } catch (SQLException e) {
        LogUtil.warn(e);
      }
    }
  }
}
