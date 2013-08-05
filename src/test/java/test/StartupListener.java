package test;

import org.eweb4j.config.EWeb4JListener;
import org.eweb4j.orm.Db;
import org.eweb4j.orm.sql.Model2Table;
import org.eweb4j.util.CommonUtil;

import test.po.Master;
import test.po.Pet;

/**
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-18 上午11:35:14
 */
public class StartupListener  implements EWeb4JListener{

	public void onStartup() {
		String sql = Model2Table.generateOne(MyEntity.class, false, false);
		System.out.println(sql);
		Db.createTableIfNotExist(MyEntity.class);
		
		Pet pet = new Pet();
		pet.setName("fuck you!");
		Master master = new Master();
		master.setName("weiwei");
		pet.setMaster(master);
		MyEntity en = new MyEntity();
		en.setData(CommonUtil.serialize(pet));
		
		Db.ar(en).create();
		
		MyEntity entity = Db.ar(MyEntity.class).findById(en.getId());
		System.out.println(entity);
		Pet aPet = CommonUtil.deserialize(entity.getData());
		System.out.println("aPet->" + aPet);
	}

	@Override
	public void onDestroy() {
		
	}

}
