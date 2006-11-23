 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/

 
/*
 * Created on 19-jun-2003
 *
 */

package be.ibridge.kettle.job.entry.job;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import be.ibridge.kettle.core.ColumnInfo;
import be.ibridge.kettle.core.Const;
import be.ibridge.kettle.core.LogWriter;
import be.ibridge.kettle.core.Props;
import be.ibridge.kettle.core.WindowProperty;
import be.ibridge.kettle.core.exception.KettleXMLException;
import be.ibridge.kettle.core.widget.TableView;
import be.ibridge.kettle.core.widget.TextVar;
import be.ibridge.kettle.job.JobMeta;
import be.ibridge.kettle.job.entry.JobEntryDialogInterface;
import be.ibridge.kettle.job.entry.JobEntryInterface;
import be.ibridge.kettle.repository.Repository;
import be.ibridge.kettle.repository.dialog.SelectObjectDialog;
import be.ibridge.kettle.trans.step.BaseStepDialog;


/**
 * This dialog allows you to edit the job job entry (JobEntryJob)
 * 
 * @author Matt
 * @since 19-06-2003
 */
public class JobEntryJobDialog extends Dialog implements JobEntryDialogInterface
{
	private LogWriter    log;
	
	private Label        wlName;
	private Text         wName;
	private FormData     fdlName, fdName;

	private Label        wlJobname;
	private Button       wbJobname;
	private TextVar      wJobname;
	private FormData     fdlJobname, fdbJobname, fdJobname;

	private Label        wlDirectory;
	private TextVar      wDirectory;
	private FormData     fdlDirectory, fdDirectory;

	private Label        wlFilename;
	private Button       wbFilename;
	private TextVar      wFilename;
	private FormData     fdlFilename, fdbFilename, fdFilename;

	private Group        wLogging;
	private FormData     fdLogging;

	private Label        wlSetLogfile;
	private Button       wSetLogfile;
	private FormData     fdlSetLogfile, fdSetLogfile;

	private Label        wlLogfile;
	private TextVar      wLogfile;
	private FormData     fdlLogfile, fdLogfile;

	private Label        wlLogext;
	private TextVar      wLogext;
	private FormData     fdlLogext, fdLogext;

	private Label        wlAddDate;
	private Button       wAddDate;
	private FormData     fdlAddDate, fdAddDate;

	private Label        wlAddTime;
	private Button       wAddTime;
	private FormData     fdlAddTime, fdAddTime;

	private Label        wlLoglevel;
	private CCombo       wLoglevel;
	private FormData     fdlLoglevel, fdLoglevel;
	
	private Label        wlPrevious;
	private Button       wPrevious;
	private FormData     fdlPrevious, fdPrevious;

    private Label        wlEveryRow;
    private Button       wEveryRow;
    private FormData     fdlEveryRow, fdEveryRow;

	private Label        wlFields;
	private TableView    wFields;
	private FormData     fdlFields, fdFields;


	private Button wOK, wCancel;
	private Listener lsOK, lsCancel;

	private Shell  shell;
	private SelectionAdapter lsDef;
	
	private JobEntryJob jobentry;
	private boolean  backupChanged;
	private Props    props;
	private Display  display;
	private Repository rep;
	
	public JobEntryJobDialog(Shell parent, JobEntryJob je, Repository rep)
	{
		super(parent, SWT.NONE);
		props = Props.getInstance();
		log   = LogWriter.getInstance();
		this.jobentry=je;
		this.rep=rep;
	}

	public JobEntryInterface open()
	{
		Shell parent = getParent();
		display = parent.getDisplay();

		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
 		props.setLook(shell);

		ModifyListener lsMod = new ModifyListener() 
		{
			public void modifyText(ModifyEvent e) 
			{
				jobentry.setChanged();
			}
		};
		backupChanged = jobentry.hasChanged();

		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText("Job entry details for this job:");
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Name line
		wlName=new Label(shell, SWT.RIGHT);
		wlName.setText("Name of job entry: ");
 		props.setLook(wlName);
		fdlName=new FormData();
		fdlName.left = new FormAttachment(0, 0);
		fdlName.top  = new FormAttachment(0, 0);
		fdlName.right= new FormAttachment(middle, 0);
		wlName.setLayoutData(fdlName);

		wName=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
 		props.setLook(wName);
		wName.addModifyListener(lsMod);
		fdName=new FormData();
		fdName.top  = new FormAttachment(0, 0);
		fdName.left = new FormAttachment(middle, 0);
		fdName.right= new FormAttachment(100, 0);
		wName.setLayoutData(fdName);

		// Jobname line
		wlJobname=new Label(shell, SWT.RIGHT);
		wlJobname.setText("Name of job: ");
 		props.setLook(wlJobname);
		fdlJobname=new FormData();
		fdlJobname.top  = new FormAttachment(wName, margin*2);
		fdlJobname.left = new FormAttachment(0, 0);
		fdlJobname.right= new FormAttachment(middle, 0);
		wlJobname.setLayoutData(fdlJobname);

		wbJobname=new Button(shell, SWT.PUSH| SWT.CENTER);
 		props.setLook(wbJobname);
		wbJobname.setText("&Browse...");
		fdbJobname=new FormData();
		fdbJobname.top   = new FormAttachment(wName, margin*2);
		fdbJobname.right = new FormAttachment(100, 0);
		wbJobname.setLayoutData(fdbJobname);
		wbJobname.setEnabled(rep!=null);

		wJobname=new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
 		props.setLook(wJobname);
		wJobname.addModifyListener(lsMod);
		fdJobname=new FormData();
		fdJobname.top  = new FormAttachment(wName, margin*2);
		fdJobname.left = new FormAttachment(middle, 0);
		fdJobname.right= new FormAttachment(wbJobname, -margin);
		wJobname.setLayoutData(fdJobname);

		// Directory line
		wlDirectory=new Label(shell, SWT.RIGHT);
		wlDirectory.setText("Repository directory: ");
 		props.setLook(wlDirectory);
		fdlDirectory=new FormData();
		fdlDirectory.top  = new FormAttachment(wJobname, margin*2);
		fdlDirectory.left = new FormAttachment(0, 0);
		fdlDirectory.right= new FormAttachment(middle, 0);
		wlDirectory.setLayoutData(fdlDirectory);

		wDirectory=new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
 		props.setLook(wDirectory);
		wDirectory.addModifyListener(lsMod);
		fdDirectory=new FormData();
		fdDirectory.top  = new FormAttachment(wJobname, margin*2);
		fdDirectory.left = new FormAttachment(middle, 0);
		fdDirectory.right= new FormAttachment(100, 0);
		wDirectory.setLayoutData(fdDirectory);
		wDirectory.setEditable(false);

		// Filename line
		wlFilename=new Label(shell, SWT.RIGHT);
		wlFilename.setText("Filename : ");
 		props.setLook(wlFilename);
		fdlFilename=new FormData();
		fdlFilename.top  = new FormAttachment(wDirectory, margin);
		fdlFilename.left = new FormAttachment(0, 0);
		fdlFilename.right= new FormAttachment(middle, 0);
		wlFilename.setLayoutData(fdlFilename);

		wbFilename=new Button(shell, SWT.PUSH| SWT.CENTER);
 		props.setLook(wbFilename);
		wbFilename.setText("&Browse...");
		fdbFilename=new FormData();
		fdbFilename.top   = new FormAttachment(wDirectory, margin);
		fdbFilename.right = new FormAttachment(100, 0);
		wbFilename.setLayoutData(fdbFilename);

		wFilename=new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
 		props.setLook(wFilename);
		wFilename.addModifyListener(lsMod);
		fdFilename=new FormData();
		fdFilename.top  = new FormAttachment(wDirectory, margin);
		fdFilename.left = new FormAttachment(middle, 0);
		fdFilename.right= new FormAttachment(wbFilename, -margin);
		wFilename.setLayoutData(fdFilename);

		// logging grouping?
		//////////////////////////
		// START OF LOGGING GROUP///
		///
		wLogging=new Group(shell, SWT.SHADOW_NONE);
 		props.setLook(wLogging);
		wLogging.setText("Log file settings");
		
		FormLayout groupLayout = new FormLayout ();
		groupLayout.marginWidth  = 10;
		groupLayout.marginHeight = 10;
		
		wLogging.setLayout(groupLayout);

		// Set the logfile?
		wlSetLogfile=new Label(wLogging, SWT.RIGHT);
		wlSetLogfile.setText("Specify logfile?");
 		props.setLook(wlSetLogfile);
		fdlSetLogfile=new FormData();
		fdlSetLogfile.left = new FormAttachment(0, 0);
		fdlSetLogfile.top  = new FormAttachment(0, margin);
		fdlSetLogfile.right= new FormAttachment(middle, -margin);
		wlSetLogfile.setLayoutData(fdlSetLogfile);
		wSetLogfile=new Button(wLogging, SWT.CHECK);
 		props.setLook(wSetLogfile);
		fdSetLogfile=new FormData();
		fdSetLogfile.left = new FormAttachment(middle, 0);
		fdSetLogfile.top  = new FormAttachment(0, margin);
		fdSetLogfile.right= new FormAttachment(100, 0);
		wSetLogfile.setLayoutData(fdSetLogfile);
		wSetLogfile.addSelectionListener(new SelectionAdapter() 
			{
				public void widgetSelected(SelectionEvent e) 
				{
					setActive();
				}
			}
		);

		// Set the logfile path + base-name
		wlLogfile=new Label(wLogging, SWT.RIGHT);
		wlLogfile.setText("Name of logfile ");
 		props.setLook(wlLogfile);
		fdlLogfile=new FormData();
		fdlLogfile.left = new FormAttachment(0, 0);
		fdlLogfile.top  = new FormAttachment(wlSetLogfile, margin);
		fdlLogfile.right= new FormAttachment(middle, 0);
		wlLogfile.setLayoutData(fdlLogfile);
		wLogfile=new TextVar(wLogging, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wLogfile.setText("");
 		props.setLook(wLogfile);
		fdLogfile=new FormData();
		fdLogfile.left = new FormAttachment(middle, 0);
		fdLogfile.top  = new FormAttachment(wlSetLogfile, margin);
		fdLogfile.right= new FormAttachment(100, 0);
		wLogfile.setLayoutData(fdLogfile);

		// Set the logfile filename extention
		wlLogext=new Label(wLogging, SWT.RIGHT);
		wlLogext.setText("Extention of logfile ");
 		props.setLook(wlLogext);
		fdlLogext=new FormData();
		fdlLogext.left = new FormAttachment(0, 0);
		fdlLogext.top  = new FormAttachment(wLogfile, margin);
		fdlLogext.right= new FormAttachment(middle, 0);
		wlLogext.setLayoutData(fdlLogext);
		wLogext=new TextVar(wLogging, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wLogext.setText("");
 		props.setLook(wLogext);
		fdLogext=new FormData();
		fdLogext.left = new FormAttachment(middle, 0);
		fdLogext.top  = new FormAttachment(wLogfile, margin);
		fdLogext.right= new FormAttachment(100, 0);
		wLogext.setLayoutData(fdLogext);

		// Add date to logfile name?
		wlAddDate=new Label(wLogging, SWT.RIGHT);
		wlAddDate.setText("Include date in filename?");
 		props.setLook(wlAddDate);
		fdlAddDate=new FormData();
		fdlAddDate.left = new FormAttachment(0, 0);
		fdlAddDate.top  = new FormAttachment(wLogext, margin);
		fdlAddDate.right= new FormAttachment(middle, -margin);
		wlAddDate.setLayoutData(fdlAddDate);
		wAddDate=new Button(wLogging, SWT.CHECK);
 		props.setLook(wAddDate);
		fdAddDate=new FormData();
		fdAddDate.left = new FormAttachment(middle, 0);
		fdAddDate.top  = new FormAttachment(wLogext, margin);
		fdAddDate.right= new FormAttachment(100, 0);
		wAddDate.setLayoutData(fdAddDate);


		// Add time to logfile name?
		wlAddTime=new Label(wLogging, SWT.RIGHT);
		wlAddTime.setText("Include time in filename? ");
 		props.setLook(wlAddTime);
		fdlAddTime=new FormData();
		fdlAddTime.left = new FormAttachment(0, 0);
		fdlAddTime.top  = new FormAttachment(wlAddDate, margin);
		fdlAddTime.right= new FormAttachment(middle, -margin);
		wlAddTime.setLayoutData(fdlAddTime);
		wAddTime=new Button(wLogging, SWT.CHECK);
 		props.setLook(wAddTime);
		fdAddTime=new FormData();
		fdAddTime.left = new FormAttachment(middle, 0);
		fdAddTime.top  = new FormAttachment(wlAddDate, margin);
		fdAddTime.right= new FormAttachment(100, 0);
		wAddTime.setLayoutData(fdAddTime);

		wlLoglevel=new Label(wLogging, SWT.RIGHT);
		wlLoglevel.setText("Loglevel ");
 		props.setLook(wlLoglevel);
		fdlLoglevel=new FormData();
		fdlLoglevel.left = new FormAttachment(0, 0);
		fdlLoglevel.right= new FormAttachment(middle, -margin);
		fdlLoglevel.top  = new FormAttachment(wlAddTime, margin);
		wlLoglevel.setLayoutData(fdlLoglevel);
		wLoglevel=new CCombo(wLogging, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
		for (int i=0;i<LogWriter.logLevelDescription.length;i++) 
			wLoglevel.add(LogWriter.logLevelDescription[i]);
		wLoglevel.select( jobentry.loglevel+1); //+1: starts at -1	
		
 		props.setLook(wLoglevel);
		fdLoglevel=new FormData();
		fdLoglevel.left = new FormAttachment(middle, 0);
		fdLoglevel.top  = new FormAttachment(wlAddTime, margin);
		fdLoglevel.right= new FormAttachment(100, 0);
		wLoglevel.setLayoutData(fdLoglevel);

		fdLogging=new FormData();
		fdLogging.left = new FormAttachment(0, margin);
		fdLogging.top  = new FormAttachment(wbFilename, margin);
		fdLogging.right= new FormAttachment(100, -margin);
		wLogging.setLayoutData(fdLogging);
		/////////////////////////////////////////////////////////////
		/// END OF LOGGING GROUP
		/////////////////////////////////////////////////////////////

		wlPrevious=new Label(shell, SWT.RIGHT);
		wlPrevious.setText("Copy prev.results to args");
 		props.setLook(wlPrevious);
		fdlPrevious=new FormData();
		fdlPrevious.left = new FormAttachment(0, 0);
		fdlPrevious.top  = new FormAttachment(wLogging, margin*3);
		fdlPrevious.right= new FormAttachment(middle, -margin);
		wlPrevious.setLayoutData(fdlPrevious);
		wPrevious=new Button(shell, SWT.CHECK );
 		props.setLook(wPrevious);
		wPrevious.setSelection(jobentry.argFromPrevious);
		wPrevious.setToolTipText("Check this to pass the results of the previous entry to the arguments of this entry.");
		fdPrevious=new FormData();
		fdPrevious.left = new FormAttachment(middle, 0);
		fdPrevious.top  = new FormAttachment(wLogging, margin*3);
		fdPrevious.right= new FormAttachment(100, 0);
		wPrevious.setLayoutData(fdPrevious);
		wPrevious.addSelectionListener(new SelectionAdapter() 
			{
				public void widgetSelected(SelectionEvent e) 
				{
					wlFields.setEnabled(!wPrevious.getSelection());
					wFields.setEnabled(!wPrevious.getSelection());
				}
			}
		);

        wlEveryRow=new Label(shell, SWT.RIGHT);
        wlEveryRow.setText("Execute once for every input row");
        props.setLook(wlEveryRow);
        fdlEveryRow=new FormData();
        fdlEveryRow.left = new FormAttachment(0, 0);
        fdlEveryRow.top  = new FormAttachment(wPrevious, margin*3);
        fdlEveryRow.right= new FormAttachment(middle, -margin);
        wlEveryRow.setLayoutData(fdlEveryRow);
        wEveryRow=new Button(shell, SWT.CHECK );
        props.setLook(wEveryRow);
        wEveryRow.setSelection(jobentry.execPerRow);
        wEveryRow.setToolTipText("Check this to execute this job mulitple times : once for every input row.");
        fdEveryRow=new FormData();
        fdEveryRow.left = new FormAttachment(middle, 0);
        fdEveryRow.top  = new FormAttachment(wPrevious, margin*3);
        fdEveryRow.right= new FormAttachment(100, 0);
        wEveryRow.setLayoutData(fdEveryRow);
        wEveryRow.addSelectionListener(new SelectionAdapter() 
            {
                public void widgetSelected(SelectionEvent e) 
                {
                    jobentry.execPerRow=!jobentry.execPerRow;
                    jobentry.setChanged();
                }
            }
        );

		wlFields=new Label(shell, SWT.NONE);
		wlFields.setText("Fields : ");
 		props.setLook(wlFields);
		fdlFields=new FormData();
		fdlFields.left = new FormAttachment(0, 0);
		fdlFields.top  = new FormAttachment(wEveryRow, margin);
		wlFields.setLayoutData(fdlFields);
		
		final int FieldsCols=1;
		int rows = jobentry.arguments==null?1:(jobentry.arguments.length==0?0:jobentry.arguments.length);
		final int FieldsRows= rows;
		
		ColumnInfo[] colinf=new ColumnInfo[FieldsCols];
		colinf[0]=new ColumnInfo("Argument",  ColumnInfo.COLUMN_TYPE_TEXT,   false);
		
		wFields=new TableView(shell, 
							  SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, 
							  colinf, 
							  FieldsRows,  
							  lsMod,
							  props
							  );

		fdFields=new FormData();
		fdFields.left = new FormAttachment(0, 0);
		fdFields.top  = new FormAttachment(wlFields, margin);
		fdFields.right  = new FormAttachment(100, 0);
		fdFields.bottom = new FormAttachment(100, -50);
		wFields.setLayoutData(fdFields);

		wlFields.setEnabled(!wPrevious.getSelection());
		wFields.setEnabled(!wPrevious.getSelection());

		// Some buttons
		wOK=new Button(shell, SWT.PUSH);
		wOK.setText("  &OK  ");
		wCancel=new Button(shell, SWT.PUSH);
		wCancel.setText("  &Cancel  ");

		BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, wFields);

		// Add listeners
		lsCancel   = new Listener() { public void handleEvent(Event e) { cancel(); } };
		lsOK       = new Listener() { public void handleEvent(Event e) { ok();     } };
		
		wOK.addListener    (SWT.Selection, lsOK     );
		wCancel.addListener(SWT.Selection, lsCancel );
		
		lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
		wName.addSelectionListener(lsDef);
		wFilename.addSelectionListener(lsDef);

		wbJobname.addSelectionListener
		(
			new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e) 
				{
                    if (rep!=null)
                    {
    					SelectObjectDialog sod = new SelectObjectDialog(shell, props, rep, false, true, false);
    					String jobname = sod.open();
    					if (jobname!=null)
    					{
    						wJobname.setText(jobname);
    						wDirectory.setText(sod.getDirectory().getPath());
    						// Copy it to the job entry name too...
    						wName.setText(wJobname.getText());
    					}
                    }
				}
			}
		);

		wbFilename.addSelectionListener
		(
			new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e) 
				{
					FileDialog dialog = new FileDialog(shell, SWT.OPEN);
					dialog.setFilterExtensions(new String[] {"*.kjb;*.xml", "*.xml", "*"});
					dialog.setFilterNames(new String[] {"Kettle jobs", "XML files", "All files"});
					
					if (wFilename.getText()!=null)
					{
						dialog.setFileName(wFilename.getText());
					}
					
					if (dialog.open()!=null)
					{
						try
						{
							wFilename.setText(dialog.getFilterPath()+Const.FILE_SEPARATOR+dialog.getFileName());
							JobMeta job = new JobMeta(log, wFilename.getText(), rep);
							if (job.getName()!=null) wName.setText(job.getName());
							else  wName.setText(dialog.getFileName()); 
						}
						catch(KettleXMLException xe)
						{
							MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
							mb.setText("Error!");
							mb.setMessage("Error reading job with file: "+wFilename.getText()+" : "+xe.getMessage());
							mb.open();
						}
					}
				}
			}
		);

		// Detect [X] or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );


		getData();
		setActive();

		BaseStepDialog.setSize(shell);

		shell.open();
		while (!shell.isDisposed())
		{
				if (!display.readAndDispatch()) display.sleep();
		}
		return jobentry;
	}

	public void dispose()
	{
		WindowProperty winprop = new WindowProperty(shell);
		props.setScreen(winprop);
		shell.dispose();
	}
	
	public void setActive()
	{
		wlLogfile.setEnabled(wSetLogfile.getSelection());
		wLogfile.setEnabled(wSetLogfile.getSelection());
		
		wlLogext.setEnabled(wSetLogfile.getSelection());
		wLogext.setEnabled(wSetLogfile.getSelection());

		wlAddDate.setEnabled(wSetLogfile.getSelection());
		wAddDate.setEnabled(wSetLogfile.getSelection());

		wlAddTime.setEnabled(wSetLogfile.getSelection());
		wAddTime.setEnabled(wSetLogfile.getSelection());

		wlLoglevel.setEnabled(wSetLogfile.getSelection());
		wLoglevel.setEnabled(wSetLogfile.getSelection());
		if (wSetLogfile.getSelection())
		{
			wLoglevel.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		}
		else
		{
			wLoglevel.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
		}
	}
	
	public void getData()
	{
		if (jobentry.getDirectory()!=null) wDirectory.setText(jobentry.getDirectory().getPath());
		if (jobentry.getName()!=null)      wName.setText(jobentry.getName());
		if (jobentry.getJobName()!=null)   wJobname.setText(jobentry.getJobName()); 
		if (jobentry.getFilename()!=null)  wFilename.setText(jobentry.getFilename());
		if (jobentry.arguments!=null)
		{
			for (int i=0;i<jobentry.arguments.length;i++)
			{
				TableItem ti = wFields.table.getItem(i);
				if (jobentry.arguments[i]!=null) ti.setText(1, jobentry.arguments[i]);
			}
			wFields.setRowNums();
			wFields.optWidth(true);
		}
		wPrevious.setSelection(jobentry.argFromPrevious);
		wSetLogfile.setSelection(jobentry.setLogfile);
		if (jobentry.logfile!=null) wLogfile.setText(jobentry.logfile);
		if (jobentry.logext!=null) wLogext.setText(jobentry.logext);
		wAddDate.setSelection(jobentry.addDate);
		wAddTime.setSelection(jobentry.addTime);

		wLoglevel.select(jobentry.loglevel+1);
	}
	
	private void cancel()
	{
		jobentry.setChanged(backupChanged);
		
		jobentry=null;
		dispose();
	}
	
	private void ok()
	{
		jobentry.setJobName(wJobname.getText());
		jobentry.setFileName(wFilename.getText());
		jobentry.setName(wName.getText());
		if (rep!=null) jobentry.setDirectory( rep.getDirectoryTree().findDirectory( wDirectory.getText() ) );
		
		int nritems = wFields.nrNonEmpty();
		int nr = 0;
		for (int i=0;i<nritems;i++)
		{
			String arg = wFields.getNonEmpty(i).getText(1);
			if (arg!=null && arg.length()!=0) nr++;
		}
		jobentry.arguments = new String[nr];
		nr=0;
		for (int i=0;i<nritems;i++)
		{
			String arg = wFields.getNonEmpty(i).getText(1);
			if (arg!=null && arg.length()!=0) 
			{
				jobentry.arguments[nr]=arg;
				nr++;
			} 
		}

        jobentry.setLogfile=wSetLogfile.getSelection();
        jobentry.addDate=wAddDate.getSelection();
        jobentry.addTime=wAddTime.getSelection();
		jobentry.logfile=wLogfile.getText();
		jobentry.logext =wLogext.getText();
		jobentry.loglevel = wLoglevel.getSelectionIndex()-1;
		dispose();
	}
}
