set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [493.2888055	489.2717563	487.712673	489.9744445	489.1839566];
y2 = [440.4319263	435.1451044	433.3213827	435.0327574	435.1685546];
y3 = [28.17353348	9.209316167	8.32244084	8.14750864	7.06770127];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the value of \theta');
ylabel('time (ms)');

axis([0.5 5.5 0.0 1000]);

%set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
set(gca, 'xtick', 1:5, 'XTickLabel', {'0.2','0.4','0.6','0.8','1.0'});
leg=legend('basic-g-v1','basic-w-v1','SWT', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);