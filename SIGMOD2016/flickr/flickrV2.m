set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [749.4844434	743.1207018	746.0629908	744.7043981	745.8693959];
y2 = [393.3473879	389.8784765	389.4454283	389.4834585	389.6224198];
y3 = [9.021544184	3.296605821	1.774915174	1.497396358	1.071001279];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the value of \theta');
ylabel('time (ms)');

axis([0.5 5.5 0.0 1250]);

%set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
set(gca, 'xtick', 1:5, 'XTickLabel', {'0.2','0.4','0.6','0.8','1.0'});
leg=legend('basic-g-v1','basic-w-v1','SWT', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);