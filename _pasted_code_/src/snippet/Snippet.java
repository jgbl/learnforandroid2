package snippet;

public class Snippet {
	public void RemoveFragSettings()
		{
			if (fPA.fragSettings != null && mPager.getCurrentItem() != SettingsActivity.fragID)
			{
				try
				{
					libLearn.gStatus = "getSupportFragmentmanager remove fragment";	
					getSupportFragmentManager().beginTransaction().detach(fPA.fragSettings).commitAllowingStateLoss();
					getSupportFragmentManager().beginTransaction().remove(fPA.fragSettings).commitAllowingStateLoss();
				}
				catch (IllegalStateException ex2)
				{
					Log.e(libLearn.gStatus, ex2.getMessage(),ex2);
				}
				fPA.destroyItem((ViewGroup)this.mainView, SettingsActivity.fragID, fPA.fragSettings);
				fPA.fragSettings=null;
			}
			
		}
		
}

