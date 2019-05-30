package com.yj.service;

import com.yj.dao.BizDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BizService {

	@Autowired
	private BizDao bizDao;

	/**
	 * 1.添加事务注解 使用propagation 指定事务的传播行为，即当前的事务方法被另外一个事务方法调用时如何使用事务。
	 * 默认取值为REQUIRED，即使用调用方法的事务 REQUIRES_NEW：使用自己的事务，调用的事务方法的事务被挂起。
	 * 2.使用isolation 指定事务的隔离级别，最常用的取值为READ_COMMITTED 
	 * 3.默认情况下Spring的声明式事务对所有的运行时异常进行回滚，也可以通过对应的属性进行设置。通常情况下，默认值即可。 
	 * 4.使用readOnly 指定事务是否为只读。表示这个事务只读取数据但不更新数据，这样可以帮助数据库引擎优化事务。若真的是一个只读取数据库值得方法，应设置readOnly=
	 * true 如果你一次执行单条查询语句，则没有必要启用事务支持，数据库默认支持SQL执行期间的读一致性；
	 * 如果你一次执行多条查询语句，例如统计查询，报表查询，在这种场景下，多条查询SQL必须保证整体的读一致性，否则，在前条SQL查询之后，
	 * 后条SQL查询之前，数据被其他用户改变，则该次整体的统计查询将会出现读数据不一致的状态，此时，应该启用事务支持。
	 * 【注意是一次执行多次查询来统计某些信息，这时为了保证数据整体的一致性，要用只读事务】
	 *  5.使用timeOut指定强制回滚之前事务可以占用的时间,默认是30秒。
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED,/*noRollbackFor={BusinessException.class},*//*readOnly=true,*/ timeout = 3)
	public void checkOut(int accountId,int bookId) throws Exception {
			try {
				bizDao.updateBookStock(bookId);
				int i=1/0;
				bizDao.updateAccountBalance(accountId);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("throw Exception");
			}
	}
}