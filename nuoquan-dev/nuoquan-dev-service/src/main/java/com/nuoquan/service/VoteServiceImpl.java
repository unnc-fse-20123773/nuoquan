package com.nuoquan.service;

import java.util.Date;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.enums.VoteStatusEnum;
import com.nuoquan.mapper.UserLikeCommentVoteMapper;
import com.nuoquan.mapper.UserMapper;
import com.nuoquan.mapper.UserVoteCommentMapper;
import com.nuoquan.mapper.UserVoteCommentMapperCustom;
import com.nuoquan.mapper.VoteImageMapper;
import com.nuoquan.mapper.VoteMapper;
import com.nuoquan.mapper.VoteMapperCustom;
import com.nuoquan.mapper.VoteOptionMapper;
import com.nuoquan.mapper.VoteUserMapper;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserLikeComment;
import com.nuoquan.pojo.UserLikeCommentVote;
import com.nuoquan.pojo.UserVoteComment;
import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.VoteImage;
import com.nuoquan.pojo.VoteOption;
import com.nuoquan.pojo.VoteUser;
import com.nuoquan.pojo.vo.UserVoteCommentVO;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.support.Convert;
import com.nuoquan.utils.PagedResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private Sid sid;
	
	@Autowired
	private VoteMapper voteMapper;
	
	@Autowired
	private VoteImageMapper voteImageMapper;
	
	@Autowired
	private VoteOptionMapper voteOptionMapper;
	
	@Autowired
	private VoteMapperCustom voteMapperCustom;
	
	@Autowired
	private UserVoteCommentMapper userVoteCommentMapper;
	
	@Autowired
	private UserVoteCommentMapperCustom userVoteCommentMapperCustom;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserLikeCommentVoteMapper userLikeCommentVoteMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private VoteUserMapper voteUserMapper;
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVote(Vote vote) {
		String id = sid.nextShort();
		vote.setId(id);
		voteMapper.insertSelective(vote);
		
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveVoteImages(VoteImage voteImage) {
		String id = sid.nextShort();
		voteImage.setId(id);
		voteImageMapper.insertSelective(voteImage);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveVoteOption(VoteOption voteOption) {
		int optionIndex = 0;
		
		String[] optionList = voteOption.getOptionContent().split("#");
		for (String opt : optionList) {
			String id = sid.nextShort();
			voteOption.setId(id);
			voteOption.setOptionContent(opt);
			voteOption.setOptionIndex(optionIndex);
			optionIndex++;
			voteOptionMapper.insertSelective(voteOption);
		}
	}
	

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult list(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		List<VoteVO> list = voteMapperCustom.list();
		for (VoteVO v : list) {
			// 为每个文章添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		
		PageInfo<VoteVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult listCheckOnly(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		List<VoteVO> list = voteMapperCustom.listCheckOnly();
		for (VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		
		PageInfo<VoteVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllVotes(Integer page, Integer pageSize, String userId) {
		
		// 从controller中获取page和pageSize(经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);
		
		List<VoteVO> list = voteMapperCustom.queryAllVotes();
		for(VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
			// 为每个投票添加用户是否投过票的布尔值
			v.setIsUserVoted(isUserVoted(userId, v.getId()));
		}
		
		PageInfo<VoteVO> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserVoted(String userId, String voteId) {
		Example example = new Example(VoteUser.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("voteId", voteId);
		List<VoteUser> list = voteUserMapper.selectByExample(example);
		
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveComment(UserVoteComment comment) {

		String id = sid.nextShort();
		comment.setId(id);
		comment.setDislikeNum(0);
		comment.setLikeNum(0);
		comment.setCreateDate(new Date());
		comment.setCommentNum(0);
		
		userVoteCommentMapper.insertSelective(comment);
		
		// 投票评论数累加
		voteMapperCustom.addVoteCommentCount(comment.getVoteId());
		if (!StringUtils.isBlank(comment.getUnderCommentId())) {
			// 主评论的评论数增加
			userVoteCommentMapperCustom.addCommentCount(comment.getUnderCommentId());
		}
		
		return id;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public UserVoteCommentVO getCommentById(String commentId, String userId) {

		return userVoteCommentMapperCustom.getCommentById(commentId);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public VoteVO getVoteById(String voteId, String userId) {

		VoteVO voteVO = voteMapperCustom.getVoteById(voteId);
		List<VoteImage> images = voteImageMapper.getVoteImgs(voteId);
		List<VoteOption> options = voteOptionMapper.getOptions(voteId);
		if (!images.isEmpty()) {
			// 添加图片列表
			voteVO.setImgList(images);
			// 添加选项列表
			voteVO.setOptionList(options);
		}
		return voteVO;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getFatherComments(Integer page, Integer pageSize, String voteId, String userId) {

		PageHelper.startPage(page, pageSize);
		
		List<UserVoteCommentVO> list = userVoteCommentMapperCustom.queryComments(voteId);
		for (UserVoteCommentVO c : list) {
			
			// 查询并设置关于用户的点赞关系
			c.setIsLike(isUserLikeComment(userId, c.getId()));
			// 查询并设置toNickName
			User user= userMapper.selectByPrimaryKey(c.getToUserId());
			c.setToNickname(user.getNickname());
		}
		
		PageInfo<UserVoteCommentVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());
		
		return grid;
	}

	@Override
	public PagedResult getSonComments(Integer page, Integer pageSize, String underCommentId, String userId) {

		PageHelper.startPage(page, pageSize);
		List<UserVoteCommentVO> list = userVoteCommentMapperCustom.querySonComments(underCommentId);
		
		for (UserVoteCommentVO c : list) {
			// 查询并设置关于用户的点赞关系
			c.setIsLike(isUserLikeComment(userId, c.getId()));
			// 设置回复人昵称
			c.setToNickname(userService.queryUserById(c.getToUserId()).getNickname());
		}
		
		PageInfo<UserVoteCommentVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public UserLikeCommentVote userLikeComment(String userId, String commentId, String createrId, Integer signFlag) {

		// 保存用户和文章的点赞关系关联表
		String likeId = sid.nextShort();
		
		UserLikeCommentVote ulcv = new UserLikeCommentVote();
		ulcv.setId(likeId);
		ulcv.setUserId(userId);
		ulcv.setCommentId(commentId);
		ulcv.setSignFlag(signFlag);
		ulcv.setCreateDate(new Date());
		
		userLikeCommentVoteMapper.insertSelective(ulcv);
		// 评论喜欢数量累加
		userVoteCommentMapperCustom.addCommentLikeNum(commentId);
		// 用户受喜欢数量的累加
		userMapper.addReceiveLikeCount(createrId);
		
		return ulcv;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeComment(String userId, String commentId, String createrId) {
		boolean isLike = isUserLikeComment(userId, commentId);
		if (isLike) {
			// 1.删除用户和文章的点赞关系关联表
			Example example = new Example(UserLikeCommentVote.class);
			// 创造条件
			Criteria criteria = example.createCriteria();
			// 条件的判断 里面的变量无需和数据库一致，与pojo类中的变量一致。在pojo类中变量与column有映射
			criteria.andEqualTo("userId", userId);
			criteria.andEqualTo("commentId", commentId);
			
			userLikeCommentVoteMapper.deleteByExample(example);
			userVoteCommentMapperCustom.reduceCommentLikeNum(commentId);
			// 3.用户受喜欢数量的累减
			userMapper.reduceReceiveLikeCount(createrId);
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeComment(String userId, String commentId) {
		Example example = new Example(UserLikeComment.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("commentId", commentId);
		
		List<UserLikeCommentVote> list = userLikeCommentVoteMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void passVote(String voteId) {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", voteId);
		Vote vote = new Vote();
		vote.setStatus(VoteStatusEnum.VOTABLE.type);
		voteMapper.updateByExampleSelective(vote, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void fDeleteVote(String voteId) {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", voteId);
		Vote vote = new Vote();
		vote.setStatus(VoteStatusEnum.UNVOTABLE.type);
		voteMapper.updateByExampleSelective(vote, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void banVote(String voteId) {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", voteId);
		Vote vote = new Vote();
		vote.setStatus(VoteStatusEnum.UNQUALIFIED.type);
		voteMapper.updateByExampleSelective(vote, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void banComment(String commentId) {
		Example example = new Example(UserVoteComment.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", commentId);
		UserVoteComment userVoteCommentHelper = new UserVoteComment();
		userVoteCommentHelper.setStatus(StatusEnum.BANNED.type);
		userVoteCommentMapper.updateByExampleSelective(userVoteCommentHelper, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void passComment(String commentId) {
		Example example = new Example(UserVoteComment.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", commentId);
		UserVoteComment userVoteCommentHelper = new UserVoteComment();
		userVoteCommentHelper.setStatus(StatusEnum.READABLE.type);
		userVoteCommentMapper.updateByExampleSelective(userVoteCommentHelper, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int updateVoteStatus(String voteIds, int status) {
		if (StringUtils.isEmpty(voteIds)) {
			return -1;
		}
		
		List<String> listId = Convert.toListStrArray(voteIds);
		
		Example example = new Example(Vote.class);
		example.createCriteria().andIn("id", listId);
		Vote a = new Vote();
		a.setStatus(status);
		return voteMapper.updateByExampleSelective(a, example);
	}


	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void selectOption(VoteUser voteUser) {
		// 1.将投票信息存入数据库
		String id = sid.nextShort();
		Date createDate = new Date();
		voteUser.setCreateDate(createDate);
		voteUser.setId(id);
		voteUserMapper.insertSelective(voteUser);
		// 2.1 vote的sum_vote(总投票数)+1
		voteMapper.addTotalVoteNum(voteUser.getVoteId());
		// 2.2 得到vote中的总票
		VoteVO voteVO = voteMapperCustom.getVoteById(voteUser.getVoteId());
		Integer totalVoteNum = voteVO.getSumVote();
//		System.out.println(totalVoteNum);
		// 3.vote_option表中的对应选项的count+1
		voteOptionMapper.addCoorespondingOptionCount(voteUser.getOptionId());
		// 4.更新vote_option表中该投票的每个选项的percent值
		Example example = new Example(VoteOption.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("vote_id");
		List<VoteOption> list = voteOptionMapper.selectByExample(example);
		for (VoteOption vu : list) {
			Integer optionTotalCount = vu.getCount();
//			System.out.println(optionTotalCount);
			Double percent =((double)optionTotalCount/totalVoteNum);
//			System.out.println(percent);
			voteOptionMapper.updatePercent(vu.getId(), percent);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public VoteVO getSingleVote(String userId, String voteId) {
		
		VoteVO v = voteMapperCustom.getSpecifiedVote(voteId);
		// 为每个投票添加图片列表
		v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
		// 为每个投票添加选项列表
		v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		// 为每个投票添加用户是否投过票的布尔值
		v.setIsUserVoted(isUserVoted(userId, v.getId()));
		
		return v;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllMyHisVote(Integer page, Integer pageSize, String userId) {

		PageHelper.startPage(page, pageSize);
		
		List<VoteVO> list = voteMapperCustom.queryAllMyHisArticle(userId);
		for (VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		PageInfo<VoteVO> voteList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(voteList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(voteList.getTotal());

		return pagedResult;
		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getOtherslegalHisVote(Integer page, Integer pageSize, String userId, String targetId) {
		
		PageHelper.startPage(page, pageSize);
		List<VoteVO> list = voteMapperCustom.queryOthersLegalHisVote(targetId);
		for (VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		PageInfo<VoteVO> voteList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(voteList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(voteList.getTotal());

		return pagedResult;
		
	}

	/**
	 * 每8分钟跟新文章状态
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void autoUpdateVoteStatus() {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andLessThan("expiryDate", new Date());
		List<Vote> vote = voteMapper.selectByExample(example);
		
		for (Vote vote2 : vote) {
			vote2.setStatus(0);
			voteMapper.updateByExampleSelective(vote2, example);
		}
	}

}











