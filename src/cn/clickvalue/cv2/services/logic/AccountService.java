package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class AccountService extends BaseService<Account> {

	public Account get(Integer userId, Integer accountId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", accountId);
		map.put("user.id", userId);
		List<Account> result = find(map);
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public Account getAlipay(Integer userId, Integer accountId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", accountId);
		map.put("user.id", userId);
		map.put("type", Integer.valueOf(1));
		List<Account> result = find(map);
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param account
	 * @return
	 * 
	 *         计算系统中有多少用户包含与account相同身份证的银行账户
	 */
	public Integer countUserForIdCardNumber(Account account) {
		CritQueryObject query = new CritQueryObject();
		query.addCriterion(Restrictions.eq("idCardNumber", account.getIdCardNumber()));
		query.addProjection(Projections.groupProperty("user.id"));
		return count(query);
	}

	/**
	 * 获取账户
	 * 
	 * @param verified
	 * @param deleted
	 * @param userId
	 * @return List
	 */
	public List<Account> findAccount(Integer verified, Integer deleted, Integer userId) {
		List<Account> accounts = new ArrayList<Account>();
		accounts = this.find(" from Account a where a.verified = ? and a.deleted = ? and a.user.id = ?", verified, deleted, userId);
		return accounts;
	}

	/**
	 * 获取支付宝帐号
	 * 
	 * @param verified
	 * @param deleted
	 * @param userId
	 * @return List
	 */
	public List<Account> findAlipay(Integer verified, Integer deleted, Integer userId) {
		List<Account> accounts = new ArrayList<Account>();
		accounts = this.find(" from Account a where a.verified = ? and a.deleted = ? and a.user.id = ? and type = 1", verified, deleted,
				userId);
		return accounts;
	}

	public boolean hasAccount(Integer verified, Integer deleted, Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("verified", verified);
		map.put("deleted", deleted);
		map.put("user.id", userId);
		return count(map) != 0 ? true : false;
	}

	public Account findDefaultAccount(Account account, Integer userId) {
		Criterion[] crits = new Criterion[4];
		crits[0] = Restrictions.eq("user.id", userId);
		crits[1] = Restrictions.ne("deleted", Integer.valueOf(1));
		crits[2] = Restrictions.eq("defaultAccount", Integer.valueOf(1));

		if (account.getId() == null) {
			crits[3] = Restrictions.isNotNull("id");
		} else {
			crits[3] = Restrictions.ne("id", account.getId());
		}

		CritQueryObject qo = new CritQueryObject(crits);

		return findUniqueBy(qo);
	}

	public Account createAccount() {
		Account account = new Account();
		account.setDeleted(Integer.valueOf(0));
		account.setCreatedAt(new Date());
		account.setDefaultAccount(0);
		account.setVerified(0);
		return account;
	}

	public Account createAlipay() {
		Account account = new Account();
		account.setDeleted(Integer.valueOf(0));
		account.setCreatedAt(new Date());
		account.setDefaultAccount(Integer.valueOf(0));
		account.setVerified(Integer.valueOf(2));
		account.setType(Integer.valueOf(1));
		return account;
	}

	/**
	 * 根据用户id和账户id获取账户
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public Account findAccount(Integer id, Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("user.id", userId);
		map.put("deleted", Integer.valueOf(0));
		map.put("type", Integer.valueOf(1));
		return findUniqueBy(map);
	}

	/**
	 * @param account
	 * @return
	 * 
	 *         查找已经审核通过或者正在审核的，相同账号的银行账号。
	 */
	public List<Account> findSameAccountByCardNumber(Account account) {
		Criterion[] criterions = new Criterion[4];
		criterions[0] = Restrictions.eq("cardNumber", account.getCardNumber());
		// 排除id
		if (account.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", account.getId());
		}
		criterions[2] = Restrictions.eq("deleted", 0);
		criterions[3] = Restrictions.in("verified", new Object[] { 1, 2 });
		CritQueryObject qo = new CritQueryObject(criterions);
		qo.addJoin("user", "user", Criteria.LEFT_JOIN);
		return find(qo);
	}

	/**
	 * @param account
	 * @return
	 * 
	 *         查找已经审核通过或者正在审核的，相同账号的银行账号。
	 */
	public List<Account> findSameAccountByIdCardNumber(Account account) {
		Criterion[] criterions = new Criterion[4];
		criterions[0] = Restrictions.eq("idCardNumber", account.getIdCardNumber());
		// 排除id
		if (account.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", account.getId());
		}
		criterions[2] = Restrictions.eq("deleted", 0);
		criterions[3] = Restrictions.in("verified", new Object[] { 1, 2 });
		CritQueryObject qo = new CritQueryObject(criterions);
		qo.addJoin("user", "user", Criteria.LEFT_JOIN);
		return find(qo);
	}

	/**
	 * @param account
	 * @param affiliate
	 * @return
	 * 
	 *         验证一个网站主名下银行账号的唯一性
	 */
	public boolean vaildateCardNumberUniqueForAffiliate(Account account, User affiliate) {
		Criterion[] criterions = new Criterion[4];
		criterions[0] = Restrictions.eq("cardNumber", account.getCardNumber());
		// 排除id
		if (account.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", account.getId());
		}
		criterions[2] = Restrictions.eq("deleted", 0);
		criterions[3] = Restrictions.eq("user.id", affiliate.getId());
		CritQueryObject qo = new CritQueryObject(criterions);
		int cnt = count(qo);
		return cnt > 0;
	}

	/**
	 * @param ids
	 * @param account
	 * @return
	 * 
	 *         判断ids对应的网站主是否包含与account的账号相同的银行帐户
	 */
	public boolean vaildateCardNumberByUserIds(List<Integer> ids, Account account) {
		if (ids == null || ids.size() == 0) {
			return false;
		}

		Criterion[] criterions = new Criterion[5];
		criterions[0] = Restrictions.eq("cardNumber", account.getCardNumber());
		// 排除id
		if (account.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", account.getId());
		}
		criterions[2] = Restrictions.eq("deleted", 0);
		criterions[3] = Restrictions.in("user.id", ids);
		criterions[4] = Restrictions.in("verified", new Object[] { 1, 2 });
		CritQueryObject qo = new CritQueryObject(criterions);
		return count(qo) > 0;
	}

	/**
	 * @param ids
	 * @param account
	 * @return
	 * 
	 *         如果传入的account的身份证在系统中已经有了，但是收款人姓名却不一样，返回false，否则true。
	 */
	public boolean vaildateIdCardNumber(Account account) {
		Criterion[] criterions = new Criterion[3];
		criterions[0] = Restrictions.eq("idCardNumber", account.getIdCardNumber());
		// 排除id
		if (account.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", account.getId());
		}
		criterions[2] = Restrictions.eq("deleted", 0);
		CritQueryObject qo = new CritQueryObject(criterions);
		Account anotherAccount = findUniqueBy(qo);
		return anotherAccount == null || anotherAccount.getOwnerName().equals(account.getOwnerName());
	}
}
