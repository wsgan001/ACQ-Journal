set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [16283.42984	15993.46573	15930.67248	15990.83746	15932.09257];
y2 = [8734.333606	8695.268036	8687.601652	8697.428906	8687.153538];
y3 = [62.78296319	26.05293136	18.89220111	16.42800125	13.8520623];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the value of \theta');
ylabel('time (ms)');

axis([0.5 5.5 0.0 18000]);

%set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
set(gca, 'xtick', 1:5, 'XTickLabel', {'0.2','0.4','0.6','0.8','1.0'});
leg=legend('basic-g-v1','basic-w-v1','SWT', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);